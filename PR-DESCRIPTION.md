## Why

The Fedora Copr build was failing, and the GitHub check hid it. Several Fedora-side changes had
accumulated:

- Fedora 44 dropped `java-21-openjdk-devel`.
- The build-logic plugins required a pinned JDK 21 toolchain the chroot cannot provide.
- The Gradle signing property was renamed `signing.gpg.enabled` → `signing.pgp.enabled`.
- `maven-local` was split per JDK; the generic package is gone on Fedora 44+.
- Fedora's packaged JUnit lags pgjdbc master (see below).

## What

By area:

1. **build-logic**: build with the current JVM when `jdkBuildVersion=0`.
2. **packaging (source distribution)**: depend on versioned `java-25-openjdk-devel`, build with
   `-PjdkBuildVersion=0`, pass the renamed `-Psigning.pgp.enabled=OFF`. A single package is used
   because Copr splits `--script-builddeps` on whitespace, and the SRPM step runs only in the
   fedora-latest chroot.
3. **packaging (RPM)**: require `(maven-local-openjdk25 or maven-local-openjdk21)` and skip the test
   suite (see below).
4. **ci**: run the build from `submit-copr.sh` and print the first error lines of a failing log
   inline. The check stays non-blocking (`continue-on-error`), but a green check no longer hides a
   failure.

### Why the test suite is skipped (`runselftest 0`)

pgjdbc master needs JUnit 5.13 to compile its tests and 5.14 for one runtime test. Stable Fedora
ships older JUnit (5.10 on f42, 5.13 on f43/f44) and does not raise JUnit's major within a released
branch. The build therefore skips the test suite by default so the package still builds against
Fedora's own dependencies. Re-enable with `--define "runselftest 1"` once Fedora ships 5.14.

## How to verify

- `./gradlew :postgresql:sourceDistribution -Prelease -PjdkBuildVersion=0 -Psigning.pgp.enabled=OFF`
  builds the tarball on the JDK in `PATH`.
- The Copr build is green across f42, f43, f44, and rawhide.

## Follow-ups

- Re-enable the test suite once Fedora ships JUnit 5.14.
- The upstream `.spec.tpl` deliberately diverges from the Fedora package spec. Agreed with the
  Fedora maintainer: our template keeps the boolean dep; their dist-git spec stays per-branch and clean.
