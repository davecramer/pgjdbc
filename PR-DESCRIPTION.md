Clarifies #3943.

Expands the `query.md` note to state that `getObject(..., OffsetDateTime.class)` always returns a
value in UTC (offset 0) — PostgreSQL stores `TIMESTAMP WITH TIME ZONE` as a UTC instant and does not
preserve the original offset — and that the session timezone does not change the returned offset.
Adds an `atZoneSameInstant(...)` example for converting to a specific zone.

Note: whether `OffsetDateTime` should instead honour the connection timezone is a separate question,
tracked in #1324.
