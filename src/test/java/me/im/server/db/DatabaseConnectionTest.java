package me.im.server.db;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {

    DatabaseConnection dbConn = null;

    @Before
    public void testDatabaseConnection() {
        dbConn = new DatabaseConnection();
    }

    @Test
    public void testQuery() {
        try {
            ResultSet resultSet = dbConn.query("select * from accounts");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
            }
            assertTrue(true);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }
}
