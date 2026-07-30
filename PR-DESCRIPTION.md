## Why

The JDBC escape `{ ? = call func(...) }` registers a single scalar OUT parameter, so the function
returns one value bound to that parameter. `Parser.modifyJdbcCall` rewrote it to
`select * from func(?) as result`, putting the function in the `FROM` clause as a set-returning
table source. For a scalar function that is the wrong invocation form: it can fail outright, or
expose the result under an unexpected column layout.

## What

When an OUT parameter precedes the function (`outParamBeforeFunc`), generate `select func(?) as result`
instead of `select * from func(?) as result`. The `{ call func(...) }` form (no OUT parameter) is
unchanged, so genuinely set-returning functions keep their table-source invocation.

## Compatibility

Applies only to the `{ ? = call func(...) }` escape. The result actually changes for only one class
of function — those returning a composite or record type. Scalar and `SETOF` functions are
unaffected apart from the result column label, which `CallableStatement` does not read.

| Function return type | `select * from f(?)` (old) | `select f(?)` (new) | `CallableStatement` impact |
| --- | --- | --- | --- |
| Scalar | 1 row, 1 column | 1 row, 1 column (labelled `result`) | None |
| `SETOF` scalar | N rows, 1 column | N rows, 1 column | None — only the label changes |
| Composite / record | fields expanded into columns | single column holding the composite | **Changes** — `getObject(1)` returns first field (old) vs whole composite (new) |

For a single registered OUT parameter the new form is more correct: `getObject(1)` returns the whole
composite, matching the one declared parameter. The composite case was not pinned by any existing test.

## How to verify

- `./gradlew :postgresql:test --tests 'org.postgresql.core.ParserTest'`
- Callable / escape-call-mode integration tests: `Jdbc3CallableStatementTest`,
  `EscapeSyntaxCallModeSelectTest`, `EscapeSyntaxCallModeCallTest`,
  `EscapeSyntaxCallModeCallIfNoReturnTest`, `CallableStmtTest`, `RefCursorTest`,
  `RefCursorFetchTest`, `ProcedureTransactionTest`.
