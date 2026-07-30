Names the policy ("five years past the `.0` of the next minor"), adds a row for older 42.x lines
still in the window, links to the Compatibility page for the resolved per-line dates, and fixes a
few prose typos.

## Why a proactive-security window?

This formalises a policy the project has effectively followed for years: older release lines get
proactive security backports, not fixes only on request. PostgreSQL itself supports each major for
five years ([versioning policy](https://www.postgresql.org/support/versioning/)); a driver whose
window is shorter would leave users unprotected while their server is still supported, so five years
is a natural anchor.

A proactive window matters because most users cannot respond to a CVE by jumping to the latest minor:

1. **Regulated change management** makes "patch the CVE and upgrade two minors at once" a non-starter.
2. **Users on frozen stacks** (legacy JVMs, old servers, pinned images) cannot freely move minors.
3. **Upgrades aren't always safe** — a newer minor can carry a regression the user cannot work around
   inside a CVE deadline.
4. **Downstream policies block minor bumps** — e.g. Spring Boot will not change a transitive
   dependency's minor version inside a patch release.

The intent matches the existing prose — separating "we are eager to fix bugs" from "we can roll
security releases" — but as a concrete, time-bounded commitment users and packagers can plan against.
