---
title: Initializing the Driver
date: 2022-06-19T22:46:55+05:30
draft: false
weight: 6
menu:
  docs:
    parent: "chapter3"
    weight: 1
---

This section describes how to load and initialize the JDBC driver in your programs.

<a name="import"></a>
# Importing JDBC

Any source that uses JDBC needs to import the `java.sql` package, using:

```java
import java.sql.*;
```

### Note

You should not import the `org.postgresql` package unless you are not using standard
PostgreSQL™ extensions to the JDBC API.