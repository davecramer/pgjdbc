## Why

[Issue #3457](https://github.com/pgjdbc/pgjdbc/issues/3457) asks the driver to use
`java.net.UnixDomainSocketAddress` (Java 16+) so local connections can go over a Unix domain socket
without a third-party dependency such as junixsocket.

## What

- Add `org.postgresql.unixsocket.UnixDomainSocketFactory`, packaged in the multi-release JAR so it
  activates automatically on Java 17+.
  - The base class (Java 8) is a stub that fails fast with a translatable `GT.tr` message.
  - The working implementation lives under `META-INF/versions/17`. `UnixDomainSocket extends Socket`
    re-exposes a Unix NIO `SocketChannel` through the subset of the `Socket` API that `PGStream`
    uses: timed reads/writes via selectors (so `socketTimeout` and async-notify peeks work), a
    `connectTimeout`-aware connect, and a concurrent close (from `Connection.abort()`), surfaced as
    a `SocketException`. TCP-only options such as `TCP_NODELAY` are accepted and ignored.
- Wire the `java17` source set into Gradle (`addMultiReleaseContents()` for `jar` and `shadowJar`),
  and add a `jdkge17` profile to `reduced-pom.xml` so the Maven source-distribution build also
  produces `META-INF/versions/17`.
- docker-compose: a `DOMAIN_SOCKET` toggle activates the server's socket listener, and
  `docker-compose.domain-socket.yml` bind-mounts the socket directory to the host.
- CI: two matrix axes.
  - `domain_socket_backend` (yes/no, Linux only) — makes the server's socket available to the host.
  - `domain_socket_frontend` (`none`/`smoke`/`all`) — controls how tests use it: `none` keeps
    everything on TCP; `smoke` runs the normal suite on TCP plus dedicated socket tests; `all` forces
    every default `TestUtil.openDB()` over the socket.

  The coverage job runs `smoke`, so one job covers both the TCP and Unix socket paths. `TestUtil`
  reads `-DdomainSocketDir` / `-DdomainSocketMode`; auth, SSL, and GSS tests pin their own properties
  and stay on TCP.
- Docs and changelog updated.

Usage:

    jdbc:postgresql://localhost/test?socketFactory=org.postgresql.unixsocket.UnixDomainSocketFactory&socketFactoryArg=/var/run/postgresql

`socketFactoryArg` is the socket directory; the URL port selects `.s.PGSQL.<port>`. The host is ignored.

## How to verify

- `META-INF/versions/17` layout confirmed in the Gradle `jar`/`shadowJar` and the Maven jar from
  `:postgresql:sourceDistribution` (with `Multi-Release: true`).
- End-to-end against PostgreSQL 16 over a Unix socket on Java 21: `SELECT 1`, `version()`, `LISTEN`,
  `connectTimeout`, `socketTimeout` all succeed. (On macOS, reach the socket from inside the Docker
  VM; it works directly on Linux CI.)
- `checkstyle`, `forbidden-apis`, `autostyle` pass for the new sources.
- `UnixDomainSocketFactoryTest` connects over the socket in `smoke`/`all` (skipped otherwise); a
  second case checks the factory builds an unconnected socket. Coverage shows once the multi-release
  JaCoCo report fix (#4256) is in master.

Closes #3457
