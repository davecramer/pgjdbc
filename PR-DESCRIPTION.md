### Why

In an updatable `ResultSet`, writing a `java.time.OffsetTime` into a `time without time zone`
column left the in-memory row buffer holding the offset (e.g. `12:34:56+05`). Reading the value
back before a `refreshRow()` or re-query then returned that offset value, disagreeing with the
`12:34:56` the database actually stores.

The persisted value was always correct: the server coerces the bound `timetz` to `time`, dropping
the offset, and a `time` has no date so there is no shift. Only the row-buffer copy was wrong, and
it self-corrects on refresh — so this is cosmetic and low severity, but worth fixing for consistency.

### What

In the `Types.TIME` branch, `setRowBufferColumn` now checks the column's OID: for `time`
(`Oid.TIME`) it stores the offset-stripped local time, matching the server; for `timetz`
(`Oid.TIMETZ`) it keeps the offset as before. Persisted behaviour is unchanged, and `OffsetTime`
into a `time` column is still accepted.

### How to verify

`./gradlew :postgresql:test --tests "org.postgresql.test.jdbc2.UpdateableResultTest"`

New tests in `UpdateableResultTest`:
- `testUpdateRowWithOffsetTimeIntoTimeColumn` — asserts both the in-ResultSet read and a fresh
  re-query return the offset-stripped local time. Fails before the fix on the in-ResultSet read.
- `testUpdateRowWithOffsetTimeIntoTimeTzColumnKeepsOffset` — guards that a `timetz` column still
  round-trips the offset.

Follow-up to #3848.
