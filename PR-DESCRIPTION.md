## Why

The 42.7.12 changelog was incomplete. Several merged PRs shipped without a `CHANGELOG.md` entry (e.g. the `search_path` `GUC_REPORT` handling on PostgreSQL 18, and the `NumberParser` overflow fix), and there was no release-notes page for the website. The `42.7.12` section also still advertised `connectThreadFactory`, replaced by `connectExecutor` before release.

## What

- Add `docs/content/changelogs/2026-06-26-42.7.12-release.md`: a website release-notes page with a curated **Notable changes** summary, `Added` / `Changed` / `Fixed` groups (`Fixed` grouped by theme), a `Translations` section, and `Commits by author` / `Contributors` lists.
- Backfill the `42.7.12` section of `CHANGELOG.md`: ~50 entries, up from ~17.
- Remove the stray `connectThreadFactory` entry, superseded by `connectExecutor`.
- Link every entry to its PR.

## How to verify

- Render the docs site (Hugo build) and open the 42.7.12 page; check the `<!--more-->` split and that links resolve.
- Skim the `42.7.12` section against `git log REL42.7.11..origin/master`.

## Follow-up

- The release date is a placeholder — set the real date and rename the file at release time.
