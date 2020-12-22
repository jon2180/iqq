package me.iqq.server.core;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 连接数据库，包装查询语句，执行查询
 */
@Deprecated
public class DatabaseConnection {

    private static final ResourceBundle dbConf;

    static {
        dbConf = ResourceBundle.getBundle("database");

        try {
            Class.forName(dbConf.getString("driver"));
        } catch (ClassNotFoundException cnfE) {
            System.out.println("Failed to load JDBC driver...");
            cnfE.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                dbConf.getString("url"),
                dbConf.getString("user"),
                dbConf.getString("password")
            );
        } catch (SQLException sqlException) {
            System.exit(1);
            sqlException.printStackTrace();
        }
        System.out.println("数据库连接成功");
        return conn;
    }

    public void __init__() {
        Properties prop = new Properties();
        InputStream iStream = Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("database.properties");
        try {
            prop.load(iStream);
            DataSource ds = DruidDataSourceFactory.createDataSource(prop);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接数据库
     */
    private DatabaseConnection() {
    }
}
