## Why

#4259 added a line saying that on PostgreSQL 18 pgJDBC "detects [search_path changes] wherever they happen and re-prepares automatically". As @sehrope noted in review, that reads as a stronger guarantee than the driver gives. `GUC_REPORT` reports changes to the `search_path` *value*, not changes in how that value resolves. A `SET ROLE` (or `SET SESSION AUTHORIZATION`) that changes which schema `"$user"` selects leaves the `search_path` string unchanged, so nothing is reported and a reused server-side prepared statement can still fail with `cached plan must not change result type` — on any server version.

This gap predates #4259 (the command-tag scan never matched `SET ROLE` either); it is documented here, not introduced.

## What

- Scope the wording to "`search_path` value changes" in both the optimisation description and recommendation 2.
- Add a note describing the `"$user"` + `SET ROLE` resolution gap and its symptom.

Documentation only; no behaviour change.

## How to verify

`cd docs && hugo --minify` builds cleanly; the rendered `documentation/server-prepare/` page shows the reworded section and the new note.

Refs #4259, #3399
