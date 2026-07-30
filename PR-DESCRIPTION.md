## Why

Since 42.7.5 the driver sends `extra_float_digits` only as a post-authentication
`SET`, decided from the real server version. A restricted session that rejects
arbitrary SQL cannot connect to a pre-12 server, because `runInitialQueries()`
issues `SET extra_float_digits = 3` and the server rejects it. This is a regression
from 42.7.3, where the value rode in the startup packet under
`assumeMinServerVersion`; reported in discussion #4306.

Why it keeps regressing (#3446, #3475, #3491, #3509, #3678): the choice between
startup packet and post-authentication `SET` for `extra_float_digits` and
`application_name` was implicit and split across `getParametersForStartup()` and
`runInitialQueries()`, driven by two different version notions —
`assumeMinServerVersion` before connecting, the real version after. Each past fix
touched one branch in isolation.

## What

- **refactor** (behavior-preserving): move the packet-vs-`SET` decision into a pure
  `InitialSessionParameters` class. `startupPacketParameters()` and
  `initialQuerySql()` read the same decision, so a parameter can no longer be sent
  twice or dropped.
- **fix**: when `assumeMinServerVersion` is in [9.0, 12), deliver
  `extra_float_digits=3` in the startup packet; the post-authentication `SET` then
  skips it. The restricted session connects, and the parameter costs no extra round
  trip.
- **tests**: `InitialSessionParametersTest` pins the whole placement matrix as a
  golden truth table; `ExtraFloatDigitsStartupTest` verifies against a live pre-12
  server that the value ends up at 3 through both channels and that the packet path
  uses one fewer round trip than the `SET` path.

This restores 42.7.3 behavior for `assumeMinServerVersion` in [9.0, 12). Unlike
42.7.3, the parameter is not sent for 12+, where it is a no-op.

### Behavior note

The decision keys on the assumed version, so two edge cases now match 42.7.3 rather
than the current behavior, and neither affects a connection that leaves
`assumeMinServerVersion` unset:
- `assumeMinServerVersion` in [9.0, 12) against a real 12+ server sets
  `extra_float_digits=3` in the packet (any value > 0 equals the 12+ default).
- A pre-9.0 server reached with `assumeMinServerVersion >= 9.0` receives 3 rather than 2.

## How to verify

- unit: `./gradlew :postgresql:test --tests org.postgresql.core.v3.InitialSessionParametersTest`
- integration against a pre-12 server: `ExtraFloatDigitsStartupTest` (verified on
  PostgreSQL 11; skipped on 16). CI runs both across the full
  (server version × `assumeMinServerVersion`) grid.
