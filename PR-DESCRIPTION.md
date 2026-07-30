Adds three CLASS-retained annotations in `org.postgresql.annotations` and applies them to all 88
`PGProperty` values. The annotations are the contract a documentation generator can read via ASM
bytecode inspection, without exposing any data at runtime. Used in #4075 to implement #1687.

---

The annotations are modeled on [apiguardian](https://github.com/apiguardian-team/apiguardian), with
a few changes:

* renamed to avoid a third-party dependency;
* added fields to track "available since / deprecated in / hidden in" (apiguardian-team/apiguardian#207);
* added `status=HIDDEN` so we can hide certain members in the bytecode later
  (apiguardian-team/apiguardian#208).

---

```
  PgApi(status, introducedIn, deprecatedIn, hiddenIn)
    Lifecycle status (STABLE / EXPERIMENTAL / MAINTAINED / INTERNAL /
    DEPRECATED / HIDDEN) plus three version axes.

  PgTags(Tag[]) — topical buckets (SSL, AUTHENTICATION, FETCH, TIMEOUT,
    NETWORK, KERBEROS_GSS, ...) plus OPERATIONS for deploy-time tuning.

  PgPropertyType(Kind) — semantic data type, with unit-bearing variants
    (DURATION_SECONDS / DURATION_MILLIS, SIZE_BYTES / SIZE_EXPRESSION) —
    surfacing "is this number seconds or milliseconds?", a long-standing
    pgjdbc foot-gun.
```

All three use `@Retention(CLASS)`: visible to bytecode readers, absent at runtime. The `Pg` prefix
avoids collisions with `org.apiguardian.api.API` (which ships with JUnit Jupiter). Each `DEPRECATED`
`@PgApi` sits alongside the standard JDK `@Deprecated`.
