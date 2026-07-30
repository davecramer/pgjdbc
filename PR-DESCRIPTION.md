## Why

`setObject(i, value, Types.TIMESTAMP)` throws a confusing error when `value` is an
`OffsetDateTime`. #3428 reports the BC-year case, which fails with `NumberFormatException`; the AD
case fails as well, choking on the `T` separator. `setObject(.., Types.DATE)` fails the same way.

## What

`Types.TIMESTAMP` and `Types.DATE` did not special-case `OffsetDateTime`, so the value was
stringified and handed to the backend parser, which rejected the ISO-8601 string
(`Trailing junk on timestamp` for AD, `NumberFormatException` for BC).

A `timestamp`/`date` without time zone has no offset, and the JDBC spec pairs `OffsetDateTime` with
`TIMESTAMP_WITH_TIMEZONE`, not `TIMESTAMP`/`DATE`. Rather than silently dropping the offset (and,
for `DATE`, the time too), the value is now rejected with a clear `INVALID_PARAMETER_TYPE` error
(`Cannot cast an instance of java.time.OffsetDateTime to type Types.TIMESTAMP`), the same way the
`Types.TIMESTAMP_WITH_TIMEZONE` branch already rejects a mismatched type.

Workarounds named in the error:
- use `Types.TIMESTAMP_WITH_TIMEZONE` to keep the instant, or
- pass a `LocalDateTime` / `LocalDate` to store the local value.

## Scope

`OffsetTime` to `Types.TIME` is intentionally unchanged: it has always been accepted and stores the
local time (dropping the offset without a shift). Rejecting it would be a breaking change for no
safety gain.

## How to verify

`SetObject310Test` (text and binary modes):
- `testSetObjectRejectsOffsetDateTimeForTimestamp` — an AD and a BC `OffsetDateTime` both fail with
  `INVALID_PARAMETER_TYPE`.
- `testSetObjectRejectsOffsetDateTimeForDate` — fails with `INVALID_PARAMETER_TYPE`.

Closes #3428
