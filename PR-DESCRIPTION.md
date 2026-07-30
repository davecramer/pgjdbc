### Why

If the response stream desyncs (corrupted bytes, MITM, a buggy server, a wire-incompatible fork),
the pre-#4015 v3 reader in `PGStream.receiveTupleV3()` trusts the 4-byte field length and does
`new byte[size]` immediately. On a corrupted length this becomes a ~1.7 GB allocation followed by an
indefinite block waiting for bytes that never arrive, and the connection is never returned to the pool.

The blast radius is worse than the per-connection cost: a broken reader stays in the pool's "open,
not yet known broken" set, gets handed to the next caller, and corrupts that caller's query too.
Every length-prefixed v3 message had this shape before #4015.

### Two classes of check

**Unconditional** checks fire on every connection in every mode; `pgjdbc.protocolHardeningMode` has
no effect on them. Each detects a value mathematically impossible on a well-formed stream, or one
that would leave the reader misaligned with the wire. Relaxing any would re-introduce #4015 or swap
a clean `IOException` for a more confusing crash a line or two later. `WARN`-and-continue is never
useful here: a real unsigned-int16 fork would need an intentional decode change, not a log line.

**Soft caps** are four length ceilings where the protocol fixes no maximum and a wire-compatible
fork could legitimately advertise more. They run at `WARNING` by default;
`pgjdbc.protocolHardeningMode` switches them to fail-fast (`fail`) or silent (`disable`).

* `NotificationResponse ≤ 1 MiB`, `ParameterStatus ≤ 1 MiB`,
  `AuthenticationRequest ≤ 8 + 2 MiB`, `AuthenticationGSSContinue ≤ 8 + 2 MiB` — each with >30×
  headroom over a well-formed message.

### Modes and compatibility

| Value | Behaviour |
|---|---|
| `fail` | Mark the `PGStream` broken and throw; opt in to fail fast on a desynced or hostile backend. |
| `warn` (default) | Log at `WARNING` with the exception, then continue with the suspect value (pre-#4015 path). |
| `disable` | Silently skip the soft-cap check. Discouraged; prefer `warn`. |

`warn` is the default so an upgrade cannot silently turn a previously-working workload into hard
failures. The unconditional set is new, but any workload that tripped it would already have crashed
or hung. `disable` relaxes only the four soft caps. Read once at class-load; configure with
`-Dpgjdbc.protocolHardeningMode=fail`.

### Tests

* `VisibleBufferedInputStreamTest` — bounded NUL-scan, partial-read resume, position tracking, budget-exceeded throw.
* `ProtocolHardeningModeTest` — all three modes, both exception factories, the case-insensitive parser, unknown-value fallback to `warn`, unconditional-check fixtures, and C-string-scan failures that set the broken flag.

### Follow-ups

Out of scope, building on the `markBroken` / `failOnDesync` machinery here:
* Message-sequencing desync in `QueryExecutorImpl.processResults` (a `removeFirstOrDesync` helper). Supersedes #1426, fixes #1425.
* COPY subprotocol and `skipMessage()` still trust the wire-provided length.
