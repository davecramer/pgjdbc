## Why

Since 42.7.11 a logical replication consumer that uses the blocking `PGReplicationStream.read()` API dies as soon as the slot is idle for one status interval, with `PSQLException: Tried to write to an inactive copy operation` ([#4328](https://github.com/pgjdbc/pgjdbc/issues/4328)).

`V3ReplicationProtocol.configureSocketTimeout` sets `SO_TIMEOUT` to `min(socketTimeout, statusInterval)` on purpose: the blocking read has to wake up periodically to send a standby status update. [b5080e76f](https://github.com/pgjdbc/pgjdbc/commit/b5080e76f) (the fix for [#3957](https://github.com/pgjdbc/pgjdbc/issues/3957), first released in 42.7.11) made `readFromCopy` end the COPY on every `IOException`, and `SocketTimeoutException` is one of them. So the routine idle timeout ended the COPY, and the status update that follows it in `V3PGReplicationStream.readInternal` then failed. Reproducing this needs a non-zero `socketTimeout`: without one, `VisibleBufferedInputStream.readMore` catches the timeout internally and retries the read, so `SocketTimeoutException` is never thrown to the caller.

The same regression hits a plain `COPY TO STDOUT` with `socketTimeout` set: after a read timeout the operation is no longer active, so `cancelCopy()` throws `Tried to cancel an inactive copy operation`. The server is left in COPY mode even though the driver has recorded the operation as finished.

## What

**A read timeout keeps the COPY active.** `readFromCopy` now catches `SocketTimeoutException` on its own and reports it without releasing the lock. This is the only failure a caller can recover from, by reading again. The driver already handles a read timeout this way in `processNotifies`.

**The COPY lock is no longer released on an I/O failure.** Releasing it treated the protocol stream as free to reuse when it was in fact desynchronized, and that is why `cancelCopy()` stopped working. Instead the driver closes the connection with `abort()` — the same thing every other I/O failure in `QueryExecutorImpl` already does — and `waitOnLock()` stops waiting once the connection is closed. This keeps the #3957 fix (operations hanging in `waitOnLock()` after a COPY died) without trying to judge the lock state from the exception type. That was the wrong test: even a `SocketTimeoutException` cannot be recovered from when it happens partway through a message, and there is no fixed list of exceptions that are always safe.

**`unlock()` now wakes every waiter.** Threads wait in `waitOnLock()` without holding the lock, so `signal()` woke only one of them and left the rest waiting for a COPY that had already ended. `signalAll()` wakes them all.

**`startCopy` and `cancelCopy` close the connection too.** Neither can be retried — a second `startCopy` would send the query twice — so keeping a connection open when its response is still in flight is worse than closing it.

**Test timeouts now run on a separate thread by default (`SEPARATE_THREAD`)** — a separate commit that can be dropped on its own. A test blocked in a socket read ignores `Thread.interrupt()`, so a same-thread timeout cannot stop it: JUnit reports the deadline only once the read returns, and a read that never returns hangs the build. Without this change, a stalled replication test becomes a hung job instead of a failed one. It also covers `PhysicalReplicationTest` and `CopyBothResponseTest`, which have no `@Timeout` of their own and relied on the global 5-minute default.

## How to verify

Six new tests, each confirmed to fail without its part of the change:

| test | what it checks |
| --- | --- |
| `CopyTest.readTimeoutKeepsCopyActive` | a starved reader gets a timeout, the COPY stays active, and `cancelCopy()` still works |
| `CopyTest.waitingThreadGivesUpWhenCopyDiesWithConnection` | a thread already waiting on the COPY lock is woken and told the connection is gone |
| `CopyTest.allWaitingThreadsResumeWhenCopyEnds` | all three waiters resume, not just one |
| `CopyTest.startCopyFailureClosesConnection` | a COPY that cannot read its start response closes the connection |
| `CopyTest.cancelCopyFailureClosesConnection` | a cancel that never reached the server closes the connection |
| `LogicalReplicationTest.idleStreamSurvivesSocketReadTimeout` | a blocking read idles through several status intervals and keeps streaming |

`TestUtil.openReplicationConnection` gained a `Consumer<Properties>` overload, mirroring `openPrivilegedDB`, so the replication test can set `socketTimeout` itself instead of depending on the CI axis.

Verified against PostgreSQL 18.4 with SSL and SCRAM, both with the plain configuration and with the matrix flags of the job that first showed this (`socketTimeout=60`, `queryTimeout=15`, `connectTimeout=7`, `autosave=always`, `cleanupSavepoints=true`, `-XX:hashCode=2`, JaCoCo): full suite 9746 tests green, autostyle and checkstyle clean.

## Notes

- The mid-message read timeout that desynchronizes the COPY stream (`Unexpected packet type during copy`) is older than this regression and is left alone. It now reaches the fatal path on the next read instead of being hidden.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
