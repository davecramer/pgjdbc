## Why

Since 42.7.13 (#4114), `PGXAConnection` leaves the caller's JDBC `autoCommit` flag
unchanged across `XAResource` calls, so an XA branch can be active while
`Connection.getAutoCommit()` still returns `true`. `BatchResultHandler` used
`getAutoCommit()` to decide whether per-statement batch progress is durable. Inside
an XA branch that check is `true` even though the transaction is still open, so a
batch that forced a mid-batch flush and then failed could report as succeeded
statements that the transaction manager later rolls back, and could collect
generated keys for rows that never committed.

This is the same `autoCommit`-as-transaction-signal problem as #4309 (the
`LargeObjectManager` regression, fixed in #4310), but on an independent path; this
PR does not close that issue.

## What

The three call sites that shared the `getAutoCommit()` check now go through one
`isProgressDurable()` helper that also requires `TransactionState.IDLE`. Progress
is secured only when each statement commits on its own, which excludes both an XA
branch and a manual `BEGIN` under auto-commit.

## How to verify

New `@Xa` regression test `XADataSourceTest.batchProgressNotSecuredWithinXaBranch`:
a large batch forces a mid-batch `Sync` (the only path that reaches
`secureProgress()` under XA), then a duplicate key fails; it asserts every entry is
`EXECUTE_FAILED` rather than a leaked committed count. The `Sync` is triggered by
the driver's 64 KB receive-buffer estimate, so it is deterministic rather than
OS-buffer dependent. Fails before the fix (`expected <-3> but was <1>`), passes after.

    ./gradlew :postgresql:test --tests "org.postgresql.test.xa.XADataSourceTest" \
      --tests "org.postgresql.test.jdbc2.BatchExecuteTest" \
      --tests "org.postgresql.test.jdbc2.BatchFailureTest"

The XA test requires `max_prepared_transactions > 0` and self-skips otherwise.
