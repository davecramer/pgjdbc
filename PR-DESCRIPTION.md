Fixes #4268

## Why

`DatabaseMetaData.getColumnPrivileges` can report a table-level privilege on a single column instead of on every column. Reported in Discussion #4265, tracked in #4268.

```sql
create user one;
create user two;
create table public.column_grant_test (c1 int, c2 int, c3 int);
grant select on column_grant_test to one;          -- table-level
grant select (c1, c2) on column_grant_test to two; -- column-level
grant update (c1, c3) on column_grant_test to two; -- column-level
```

`getColumnPrivileges(null, "public", "column_grant_test", null)` returns `SELECT` for `one` only on `c3`, even though `one` holds a table-wide `SELECT` covering every column.

## Root cause

The method parses the table ACL (`relacl`) and the column ACL (`attacl`) into a `privilege -> grantee -> [grantor, grantable]` map, then combines them with `Map.putAll`. `putAll` replaces whole per-privilege entries, so for each column the table-level grant was:

- **dropped** from any privilege that also appeared at the column level (`one`'s `SELECT` vanished from `c1`/`c2`, whose `attacl` carries a column-level `SELECT` for `two`), and
- **leaked** onto columns where that privilege had no column-level entry to overwrite it (`one`'s `SELECT` survived on `c3`, whose `attacl` is `UPDATE`-only).

## What

Merge the table-level and column-level maps per privilege and grantee — union and de-duplicate identical `grantor`/`grantable` pairs — instead of replacing. A table-level grant now applies to every column (matching `information_schema.column_privileges`), and a column with no column-level ACL keeps its table-level grants unchanged.

## How to verify

New regression test `org.postgresql.test.jdbc2.ColumnPrivilegesTest`:

- `mergesTableAndColumnLevelGrants` — full per-column privilege set for the case above. Fails on `master`, passes here.
- `deduplicatesGrantsHeldAtBothLevels` — the same privilege at both levels is reported once.
- `reportsColumnLevelPrivilegeNotHeldAtTableLevel` — a column-only grant is still reported.
- `keepsTableAndColumnGrantsThatDifferInGrantOption` — grantable table-level vs non-grantable column-level are kept as separate rows.
- `reportsColumnGrantWhenNoTableLevelGrantsExist` — a column grant on a table with no table-level ACL is reported.

```
./gradlew :postgresql:test --tests 'org.postgresql.test.jdbc2.ColumnPrivilegesTest'
```
