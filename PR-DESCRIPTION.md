## Why

The existing `binaryTransfer`, `binaryTransferEnable`, and `binaryTransferDisable` properties
cannot distinguish the **send** direction (parameters sent to the server) from the **receive**
direction (results read back). The driver already keeps the two OID sets separate internally, and
the directions differ: for example, the driver excludes `date` from binary on send to preserve
sub-day precision for timestamp targets, which has no bearing on receive. A single property cannot
express both.

Splitting configuration by direction also makes #3062 easier to configure. This can land before or
as part of #3062.

## What

Two new connection properties in `oid:mode` format (`oid` is a type name or OID number):

- `binarySend` — modes `auto`, `force`, `disable`.
- `binaryReceive` — modes `auto`, `disable`.

Semantics per type and direction:

- `force` (send only) adds the type to the binary set, with the same risk as `binaryTransferEnable`
  (the server may reject binary for a type, and for temporal types it can change the stored value).
- `disable` keeps the type in text and stops the legacy properties from re-enabling it.
- `auto` resets the type to the driver's built-in default, overriding the legacy properties
  (including `binaryTransferDisable`), so the type also leaves the disabled set.

Precedence: a per-type mode here wins over the legacy `binaryTransfer*` properties for that type and
direction. The legacy properties are otherwise unchanged. Scope: top-level types only; `auto` may
change between driver versions; recursion into composite/array element types can follow later. This
is a thin wrapper over the per-direction OID sets the driver already maintains — no codec-layer work.

Also adds matching `BaseDataSource` getters/setters and documents the properties in `README.md` and
`docs/content/documentation/use.md`.

## How to verify

    ./gradlew --quiet :postgresql:classes :postgresql:style
    ./gradlew --quiet :postgresql:test --tests org.postgresql.test.jdbc2.BinaryDirectionPropertiesTest

`BinaryDirectionPropertiesTest` covers `force`/`disable`/`auto`, precedence over the legacy
properties in both directions, OID by name and by number, and rejection of invalid modes/syntax.
