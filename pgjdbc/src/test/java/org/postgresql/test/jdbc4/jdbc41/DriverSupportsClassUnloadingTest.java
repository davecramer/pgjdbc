/*
 * Copyright (c) 2023, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

package org.postgresql.test.jdbc4.jdbc41;

import org.postgresql.Driver;
import org.postgresql.test.TestUtil;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.jiderhamn.classloader.leak.JUnitClassloaderRunner;
import se.jiderhamn.classloader.leak.Leaks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

@RunWith(JUnitClassloaderRunner.class)
public class DriverSupportsClassUnloadingTest {

  @BeforeClass
  public static void setSmallCleanupThreadTtl() {
    // Make the tests faster
    System.setProperty("pgjdbc.config.cleanup.thread.ttl", "100");
  }

  @AfterClass
  public static void resetCleanupThreadTtl() {
    System.clearProperty("pgjdbc.config.cleanup.thread.ttl");
  }

  @Test
  @Leaks(false)
  public void driverUnloadsWhenConnectionLeaks() throws SQLException, InterruptedException {
    try {
      if (!Driver.isRegistered()) {
        Driver.register();
      }
      // This code intentionally leaks connection, prepared statement to verify if the classes
      // will still be able to unload
      Connection con = TestUtil.openDB();
      PreparedStatement ps = con.prepareStatement("select 1 c1, 'hello' c2");
      ResultSetMetaData md = ps.getMetaData();
      Assert.assertEquals(".getColumnType for column 1 c1 should be INTEGER", md.getColumnType(1),
          Types.INTEGER);

      ResultSet rs = ps.executeQuery();
      rs.next();
      Assert.assertEquals(".getInt for column c1", rs.getInt(1), 1);
    } finally {
      // We need deregister the driver explicitly, otherwise DriverManager will keep reference on
      // the driver
      Driver.deregister();
    }
    JUnitClassloaderRunner.forceGc(3);
    // Allow for the cleanup thread to terminate
    Thread.sleep(2000);
  }

  @Test
  @Leaks(value = false, dumpHeapOnError = true)
  public void driverUnloadsWhenConnectionClosedExplicitly() throws SQLException,
      InterruptedException {
    try {
      if (!Driver.isRegistered()) {
        Driver.register();
      }
      // This code intentionally leaks connection, prepared statement to verify if the classes
      // will still be able to unload
      try (Connection con = TestUtil.openDB();) {
        try (PreparedStatement ps = con.prepareStatement("select 1 c1, 'hello' c2");) {
          ResultSetMetaData md = ps.getMetaData();
          Assert.assertEquals(".getColumnType for column 1 c1 should be INTEGER",
              md.getColumnType(1), Types.INTEGER);

          try (ResultSet rs = ps.executeQuery();) {
            rs.next();
            Assert.assertEquals(".getInt for column c1", rs.getInt(1), 1);
          }
        }
      }
    } finally {
      // We need deregister the driver explicitly, otherwise DriverManager will keep reference on
      // the driver
      Driver.deregister();
    }
    // Allow for the cleanup thread to terminate
    Thread.sleep(2000);
  }
}
