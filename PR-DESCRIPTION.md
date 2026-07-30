## Why

Before 42.7.13, XA `start()` set `autoCommit=false` for the duration of the branch
and restored it on end/commit/rollback, so `LargeObjectManager` saw an open
transaction and allowed large objects. #4114 removed that save/restore, so
`start()` no longer touches `autoCommit`; it stays `true` while the branch is open.

`LargeObjectManager.createLO()` / `open()` use `getAutoCommit()` as a proxy for
whether a transaction is open, so since 42.7.13 they wrongly fail with "Large
Objects may not be used in auto-commit mode" inside an XA branch — a regression from
42.7.12, reported in #4309.

## What

Gate `createLO`/`open` on the server transaction state: refuse only when
`autoCommit && TransactionState.IDLE`. A running transaction — an XA branch or a
manual `BEGIN` under auto-commit — now permits large objects, while a plain
auto-commit connection is still refused.

## How to verify

New tests:
- `XADataSourceTest.largeObjectWithinXaBranch` (`@Xa`) — create/write/read a large
  object inside an active XA branch. Fails before the fix, passes after.
- `LargeObjectManagerTest.refusesInAutoCommitModeWithoutTransaction` — pins the
  negative side so the guard cannot be silently dropped.

    ./gradlew :postgresql:test --tests "org.postgresql.test.xa.XADataSourceTest" \
      --tests "org.postgresql.jdbc.LargeObjectManagerTest"

The XA test requires `max_prepared_transactions > 0` and self-skips otherwise.

## Related

The sibling `autoCommit`-as-transaction-signal problem in `BatchResultHandler` is
fixed separately in #4311.
