/*
 * Copyright (c) 2023, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

package org.postgresql.test.util;

import static org.junit.Assert.assertEquals;

import org.postgresql.PGProperty;
import org.postgresql.core.ServerVersion;
import org.postgresql.test.TestUtil;
import org.postgresql.util.PasswordUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class PasswordUtilTest {
  Connection con;

  @Before
  public void setUp() throws Exception {
    Properties properties = new Properties();
    PGProperty.PREFER_QUERY_MODE.set(properties, "simple");
    con = TestUtil.openDB(properties);
  }

  @After
  public void tearDown() throws SQLException {
    con.close();
  }

  @Test
  public void testGetEncryption() throws SQLException {
    if (TestUtil.haveMinimumServerVersion(con, ServerVersion.v10)) {
      assertEquals("scram-sha-256", PasswordUtil.getEncryption(con));
      setEncryption("md5");
      assertEquals("md5", PasswordUtil.getEncryption(con));
      setEncryption("\"scram-sha-256\"");
    }
  }

  @Test
  public void testScramPassword() throws SQLException {
    String user = TestUtil.getUser();
    String encryption = getEncryptionForUser(user);
    if (encryption.equalsIgnoreCase("none")) {
      return;
    }
    PasswordUtil.alterPassword(con, TestUtil.getUser(), TestUtil.getPassword(), encryption);
    con.close();
    con = TestUtil.openDB();
  }

  /***
   * @param user user we want the encryption for
   * @return md5 or scram-sha-256
   * @throws SQLException
   We use this instead of asking the server since the password encryption could be different for
   each user
   */
  private String getEncryptionForUser(String user) throws SQLException {
    try (Connection conn = TestUtil.openPrivilegedDB();
          Statement statement = conn.createStatement();
          ResultSet rs = statement.executeQuery("select passwd from pg_shadow where usename = " + user)) {
      while (rs.next()) {
        String encryption = rs.getString(1);
        if (rs.wasNull()) {
          return "none";
        } else {
          if (encryption.startsWith("md5")) {
            return PasswordUtil.MD5;
          } else {
            return PasswordUtil.SCRAM_ENCRYPTION;
          }
        }
      }
    }
    return "none";
  }

  private void setEncryption(String encryption) throws SQLException {
    try (Statement statement = con.createStatement()) {
      statement.execute("set password_encryption to " + encryption);
    }
  }
}
