package com.nxt.im.db;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {
  @Test
  public void testDatabaseConnection() {
    DatabaseConnection databaseConnection = new DatabaseConnection();
    // try {
    //   Thread.sleep(1000);
    // } catch (InterruptedException iE) {
    //   assertTrue(false);
    //   iE.printStackTrace();
    //   return;
    // }
    databaseConnection.close();
    assertTrue(true);
  }
}