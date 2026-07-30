Documentation rework, structure only: file moves, Hugo template skeleton, lunr-based site search, a
front-matter-driven release pipeline, and CI wiring. Prose is byte-identical to master in every page
this PR touches.

## What and why

The documentation grew around a flat, historically-shaped set of files. Important topics —
connection setup, SSL/TLS, prepared statements, PostgreSQL extensions — were buried in oversized
pages or hard to find in the navigation. The release-presentation side had a parallel problem: the
home page, download page, and per-version cards were each driven by hand-maintained config
(`versions.toml`, `homepagedata.toml`), so cutting a release meant editing several files in lockstep.

This PR sets up the new shape of the site so the prose work that follows has somewhere to land:

* reorganises `docs/content/documentation/` into task-oriented sections (`connect`, `data-types`,
  `getting-started`, `postgresql-features`, `query`, `runtime`, `security`);
* slices the four largest legacy pages (`use.md`, `setup.md`, `server-prepare.md`, `query.md`) at
  their H2 boundaries into smaller topical pages, prose verbatim;
* installs the Hugo template skeleton, the SCSS bundle, and lunr site search with a
  camelCase/snake_case tokeniser;
* replaces `versions.toml` / `homepagedata.toml` with a release pipeline that reads
  `/changelogs/*-release.md` front-matter directly, plus a `release-history-overlay.yaml` for facts
  that cannot be derived from git;
* wires `:docs-tools:buildDocs` / `:docs-tools:serveDocs` into a Gradle module and adds the GitHub
  Pages deploy workflow.

## Reviewing this PR

There is no prose to read; the check is mechanical.

1. **Build and click through.** `./gradlew --quiet :docs-tools:serveDocs`, then open
   <http://localhost:1313/> and follow links from the home page through the sidebar. Every page
   renders, every legacy URL resolves via meta-refresh, and fragments are preserved.
2. **Link check.** `lintDocsLinks` runs as part of `buildDocs` and fails on root-relative `href="/..."`.
3. **Verify byte-equality.** Pick any row from the provenance map and run its diff recipe. Empty
   output means the new page is byte-identical to the cited master range.

<details>
<summary><b>Provenance map: where every page comes from</b></summary>

[... provenance tables and recipes unchanged ...]

</details>
