> **Draft, stacked on #4299.** The first commit here is that PR's; the diff against `master` includes it. Review the nine commits on top — the diff collapses to those once #4299 merges and this rebases.

## Why

One SQL text owns exactly one server-prepared statement. When the same `PreparedStatement` runs with different parameter-type signatures — `setInt` on one call, `setString` on the next, or Spring's `setNull` binding `unspecified` where the previous run bound `int4` — the driver unprepares and re-parses under a new name on every switch. That costs a `Parse` round trip per switch and discards the server-side plan, which hurts most when the plan depends on the parameter type.

This is the per-connection half of #345. The cross-connection half (sharing one parse across a pool) is **not** implemented here; it is written up as Problem B in `typed_statement_cache_design.md`.

## What

Two behavior-changing knobs, both defaulting to today's behavior, plus the refactoring that enables them.

Behavior-identical refactorings:
* `refactor: extract server statement state from SimpleQuery into ServerHandle` — statement name, prepared types, deallocate epoch, row description, format flags, and the cleanup phantom reference move into `ServerHandle`; `SimpleQuery` delegates to a single permanent instance.
* `refactor: route protocol responses to the statement they were sent for` — every outgoing message and pending-queue entry carries a `ServerHandle` snapshot, so a late response can't corrupt a statement the query has since moved off.

Behavior changes:
* `feat: pin a statement while a portal bound from it is open` — a `Portal` pins its source handle at construction, released once through a shared cleanup. Also fixes two GC-dependent leaks: portals that never reached `BindComplete`, and portals whose `Execute` failed.
* `feat: keep the named statement intact across one-shot executions` — a one-shot execution (e.g. `getParameterMetaData()` after another type was bound) used to unprepare the named statement; it now falls back to a per-query unnamed statement.
* `feat: make the pre-describe gates parameter- and epoch-aware` — the JDBC-layer gates that skip a describe round trip read `Query.isStatementDescribed()`, which knows nothing about upcoming parameter types or invalidation, so stale describe results survived DDL, `SET search_path`, and `DEALLOCATE ALL`. The new `QueryExecutor.isStatementDescribed(Query, ParameterList, flags)` resolves the statement the execution would actually use.
* `feat: keep one server-prepared statement per parameter-type signature` — one SQL text may now hold several named statements, one per signature, LRU-evicted and capped by `preparedStatementCacheTypeVariants` (default `4`; `1` restores the old behavior). An application binding stable types keeps a single handle at any budget — the extra slots are allocated lazily, so the default costs it nothing. Handles from an older deallocate epoch are dropped lazily during lookup.
* `feat: cap server-prepared statements per connection` — `maxServerPreparedStatements` (default `0` = no limit) bounds the named statements a connection keeps across all SQL texts, closing the least recently used in the execution preamble. Statements pinned by an open cursor are exempt, so the limit is soft. The recency map is allocated only when a limit is set.

## Benchmark

`benchmarks/.../statement/TypeVariants.java`: server-prepared `SELECT ?`, `prepareThreshold=1`, PostgreSQL in Docker on the same host. `typeVariants=1` is the old behavior; `typeVariants=2` exercises the new path.

| Scenario | `typeVariants=1` | `typeVariants=2` |
|---|---|---|
| `sameSignature` (must not regress) | 3121.34 B/op | 3121.38 B/op |
| `alternatingSignatures` | 191.7 ± 23.2 µs/op, 3545.5 B/op | 158.6 ± 16.2 µs/op, 3161.7 B/op |

Same-signature allocation is byte-identical. Alternating signatures run ~17% faster (36 samples/setting, 3 forks) and allocate like the fixed-signature case, since the `Parse` traffic is gone. Treat 17% as a lower bound: it prices only `Parse` traffic on localhost, ignoring both the plan-quality benefit and the round-trip cost that network latency would add.

## Open questions

1. **Default for `preparedStatementCacheTypeVariants`.** Ships as `4`. Is 4 right, and is `maxServerPreparedStatements` (off by default) an adequate backstop?
2. **One PR or a series?** The two refactorings are behavior-preserving and could land separately. Happy to split.
3. **`typed_statement_cache_design.md`** sits at the repo root for this discussion. Let me know and I'll move or drop it.
4. **#345's cross-connection sharing** is designed but unimplemented. Worth pursuing after this?
