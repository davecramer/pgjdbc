## Why

In an updatable `ResultSet`, updating a `timestamp without time zone` column with an
`OffsetDateTime` stored a session-zone-dependent value (and a corrupt result for BC dates) instead
of failing. Updating a `date` column with an `OffsetDateTime` threw a `ClassCastException`.

Follow-up to #3848, which added `java.time` support to `updateRow()` / `insertRow()`.

## What

`updateRow()` / `insertRow()` bind values via `setObject()` with no target type, so an
`OffsetDateTime` reaches the server as `timestamptz`. For a `timestamp without time zone` column the
server shifts the value into the session time zone before storing it (with a `Europe/Moscow`
session):

| Value | Silently stored before |
| --- | --- |
| `2024-01-31T12:34:56+05` | `2024-01-31 10:34:56` |
| `0003-01-01T00:00+05` (BC) | `0004-12-31 21:30:17 BC` |

A `timestamp`/`date` without time zone has no offset, and the JDBC spec pairs `OffsetDateTime` with
`TIMESTAMP_WITH_TIMEZONE`. The value is now rejected with a clear `INVALID_PARAMETER_TYPE` error
(`Cannot cast an instance of java.time.OffsetDateTime to type timestamp`) instead of being silently
corrupted (or throwing `ClassCastException` for a `date` column). This matches the contract of
`setObject(i, value, Types.TIMESTAMP)` from #4228. `timestamptz` columns are unchanged.

Workarounds named in the error:
- update a `timestamptz` column to keep the instant, or
- pass a `LocalDateTime` / `LocalDate` (`offsetDateTime.toLocalDateTime()`) to store the local value.

## Scope

`OffsetTime` into a `time without time zone` column is intentionally still accepted: it stores the
local time (dropping the offset without a shift, since a `time` has no date) — a non-destructive
conversion, not a corruption to reject.

## How to verify

`UpdateableResultTest`:
- `testUpdateRowRejectsOffsetDateTimeForTimestampColumn` — an AD and a BC `OffsetDateTime` into a
  `timestamp` column both fail with `INVALID_PARAMETER_TYPE`.
- `testUpdateRowRejectsOffsetDateTimeForDateColumn` — an `OffsetDateTime` into a `date` column fails
  with `INVALID_PARAMETER_TYPE`.
