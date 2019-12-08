package com.nxt.im.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nxt.im.config.Database;

/**
 * 连接数据库，包装查询语句，执行查询
 */
public class DatabaseConnection {
  public Connection connection = null;
  public PreparedStatement pStatement = null;

  public ResultSet resultSet = null;

  /**
   * 连接数据库
   */
  public DatabaseConnection() {
    String driver = "com.mysql.cj.jdbc.Driver";

    String dbUrl = "jdbc:mysql://" + Database.IP + ":" + Database.PORT + "/" + Database.TABLE_NAME
        + "?useUnicode=true&characterEncoding=UTF-8";

    try {
      Class.forName(driver);
      connection = DriverManager.getConnection(dbUrl, Database.USER, Database.PASSWORD);
      System.out.println("数据库连接成功");
    } catch (SQLException | ClassNotFoundException cnfE) {
      System.out.println("无法连接到数据库：" + cnfE.getMessage());
      cnfE.printStackTrace();
    }
  }

  /**
   * 查询
   * 
   * @param sqlStatement 查询语句
   * @return
   */
  public ResultSet query(String sqlStatement) {
    try {
      pStatement = connection.prepareStatement(sqlStatement);
      resultSet = pStatement.executeQuery();
    } catch (SQLException sqlE) {
      sqlE.printStackTrace();
    }
    return resultSet;
  }

  /**
   * 更新
   * 
   * @param sqlStatement 更新语句
   */
  public void update(String sqlStatement) {
    try {
      pStatement = connection.prepareStatement(sqlStatement);
      pStatement.executeUpdate();
    } catch (SQLException sqlE) {
      sqlE.printStackTrace();
    }
  }

  public void close() {
    try {
      if (resultSet != null && !resultSet.isClosed()) {
        resultSet.close();
      }
    } catch (SQLException sqlE) {
      System.out.println("结果集关闭异常: " + sqlE.getMessage());
      sqlE.printStackTrace();
    }
    try {
      if (pStatement != null && !pStatement.isClosed())
        pStatement.close();
    } catch (SQLException e) {
      System.out.println("更新集关闭异常：" + e.getMessage());
    }
    try {
      if (connection != null && !connection.isClosed())
        connection.close();
    } catch (SQLException e) {
      System.out.println("数据库连接关闭异常：" + e.getMessage());
    }

  }

  public static void main(String[] args) {
    DatabaseConnection databaseConnection = new DatabaseConnection();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException iE) {
      iE.printStackTrace();
    }
    databaseConnection.close();
  }
}