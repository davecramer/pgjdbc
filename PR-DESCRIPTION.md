## Why

`PgPreparedStatement.getParameterMetaData()` issues a `Describe(statement)` round trip on every call. Frameworks that read parameter metadata per row (e.g. Spring's `setNull` path) incur one network round trip per parameter set, and that dominates batch insert time on high-latency links.

Closes #621. Supersedes #3429.

## What

`SimpleQuery` now caches up to four describe results as (requested types, server-resolved types) pairs, in most-recently-used order. `getParameterMetaData()` asks the new `QueryExecutor.tryResolveParameterTypes` first and describes only on a cache miss.

A cached result is reused only for a compatible set of parameter types: a set type must equal the resolved type, and an unset type is compatible only when the describe request had it unset too (the server infers unspecified types from the specified ones — consider overloaded functions). A type change forces a re-describe, and an ambiguity error is never masked by the cache.

The results live on `SimpleQuery`, shared across `PreparedStatement` instances via the connection query cache and evicted with the query. They survive `unprepare()`: they capture how the server resolves types for the SQL text, not the state of a server-side statement.

Each result is stamped with the `deallocateEpoch` at which it was captured; an epoch mismatch is a cache miss. Any event that could make the server resolve types differently bumps the epoch and so discards the results — DDL and `SET search_path` both do. Reusing that counter also flushes on `DEALLOCATE ALL` (one extra describe), cheaper than a second invalidation counter a future epoch bump could forget to update.

The execute path is unchanged and the describe stays `QUERY_ONESHOT`, so `prepareThreshold` semantics are unaffected.

## Memory accounting

The cached results grow with parameter count, not SQL text length. At 10000 parameters each type-OID array is ~40 KB, so four results retain ~320 KB per query, on top of the ~40 KB `preparedTypes` already held.

This memory was invisible to the `preparedStatementCacheSizeMiB` budget: `CachedQuery.getSize()` estimated an entry from SQL text length alone (`queryLength * 2 + 100`). `Query` now gains `getRetainedSizeExcludingSql()`; `SimpleQuery` reports its prepared types plus cached describe results. Consequence: `LruCache.put` refuses an entry larger than half the cache, so a query with enough parameters can now be dropped where it silently could not before (margin is wide at the 5 MiB default — ~360 KB at 10000 parameters).

## How to verify

- `ParameterMetaDataRoundtripTest` (new): counts describe round trips; covers cache hits, type-change misses, epoch invalidation after DDL and `SET search_path`, and behavior across `unprepare()`.
- `SimpleQueryDescribeCacheTest` (new): unit coverage of the cache (compatibility rule, MRU ordering, epoch handling).
- `SimpleQueryRetainedSizeTest` (new): the size accounting.

      ./gradlew :postgresql:test \
        --tests '*ParameterMetaDataRoundtripTest*' \
        --tests '*SimpleQueryDescribeCacheTest*' \
        --tests '*SimpleQueryRetainedSizeTest*'
