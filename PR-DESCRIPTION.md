## Why

`docker/postgres-server/docker-compose.yml` is shared across git worktrees. Compose derives the
project name from the directory, so every worktree is treated as the **same** project: shared
network, shared container, and a `run --rm` or `down` in one worktree tears down the server another
is using. The published ports (5432/5433/5434) are fixed too, so two servers cannot run side by side.

This affects anyone running work in parallel across worktrees — several branches, or multiple
agents, sharing one machine.

## What

Opt-in isolation gated by a single environment variable. Default behaviour is unchanged.

- `docker/bin/postgres-server`: when `PG_ISOLATE=1`, derive a unique `COMPOSE_PROJECT_NAME` and a
  host-port offset from `git rev-parse --show-toplevel` (a `cksum` of the path → slot 0–199 →
  offset ×10). Unset keeps the historic project name and ports 5432/5433/5434, so CI is untouched.
  An explicit `PG_PUBLISH_PORT` still wins. Set it once via `direnv` or your shell profile.
- `docker-compose.yml`: port mappings now substitute `PG_PUBLISH_PORT` /
  `PG_REPLICA_ONE_PUBLISH_PORT` / `PG_REPLICA_TWO_PUBLISH_PORT` (with the old defaults).
- Fix a copy-paste bug: the second replica port read `PG_REPLICA_ONE_PUBLISH_PORT` instead of
  `PG_REPLICA_TWO_PUBLISH_PORT`.

## How to verify

    cd docker/postgres-server
    docker compose config | grep -E 'name:|published:'        # default
    PG_ISOLATE=1 ../bin/postgres-server                       # isolated — distinct ports/project

## Draft — open questions

Deliberately not included yet:
- Writing the chosen ports into `build.local.properties` so `./gradlew test` in a worktree hits its
  own server. Without it, tests still target 5432 when isolation is on.
- Slot-collision handling. Two paths that hash to the same `cksum % 200` would share ports; the
  robust alternative is ephemeral host ports read back via `docker compose port`.

Heavier alternatives (a Gradle shared `BuildService`, Testcontainers) change the CI flow; happy to
discuss if reviewers prefer one.
