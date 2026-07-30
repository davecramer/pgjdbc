## Why

`CallableStatement`'s by-name API — `setXXX(String)`, `getXXX(String)`, and
`registerOutParameter(String, ...)` — threw `notImplemented`, forcing positional indexes. This
implements it. Extracted from the larger `typecache` work to land early and shrink that branch's diff.

## What

- Resolve a parameter name to its 1-based JDBC index via `pg_catalog.pg_proc` through a `regproc`
  cast, applying the same `search_path` and quoting rules as the call site.
- Handle the two escape forms separately: `{ call proc(...) }` keeps arguments in declaration order,
  while `{ ? = call f(...) }` reserves index 1 for the return placeholder and maps input arguments
  to 2, 3, and so on.
- Cache the name-to-index map on `CachedQuery`, stamped with a per-connection type-cache epoch. The
  executor bumps the epoch on DDL and on `SET search_path`, so a redefined routine rebuilds the map
  instead of binding to a stale index. `getTypeCacheEpoch()` is exposed through `QueryExecutor`,
  `BaseConnection`, and `PgConnection`.
- Wire the `SQLType` `registerOutParameter` overloads and implement `getCharacterStream(int)`.

STRUCT/SQLData OUT decoding and typemap `getObject` stay in the `typecache` branch and remain
`notImplemented` here.

## How to verify

`Jdbc3CallableStatementTest` adds `testSumByParameterName`,
`testNamedParameterAfterUnnamedResolvesToCorrectIndex`, `testOutParameterByNameInCallForm`,
`testInputParameterNamedLikeReturnAlias`, and `testNamedParameterMapInvalidatedAfterDdl`.

    ./gradlew :postgresql:test --tests org.postgresql.test.jdbc3.Jdbc3CallableStatementTest

## Known limitations (deliberately not addressed here)

Inherited from the `typecache` branch and left as-is to keep the planned rebase clean:

- **Overloaded routines.** The `regproc` cast resolves by name only, so an overloaded routine raises
  an actionable error even when explicit casts would disambiguate. A failed cast also aborts an open
  transaction.
- **Case-distinct quoted identifiers.** Catalog names fold to lower case, so `"A"` and `"a"` collapse
  to one key.
- **Custom `SQLType` vendor numbers.** Mapped by name, falling back to `Types.OTHER` rather than
  consulting `SQLType.getVendorTypeNumber()`.

Separately, `SET search_path` is detected case-sensitively (pre-existing); case-insensitive detection
is handled in #4216.
