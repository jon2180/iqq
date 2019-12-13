package com.nxt.im.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nxt.im.common.Accounts;
import com.nxt.im.common.DataByteBuffer;
import com.nxt.im.db.DatabaseConnection;

/**
 * 统一分发处理路由
 * 
 * @description 在考虑具体实现方式
 * @version 191211
 */
public class Router {

  private static DatabaseConnection dbConnection;

  static {
    dbConnection = new DatabaseConnection();
  }

  private Router() {

  }

  public static void dispatch(SocketChannel socketChannel, ByteBuffer byteBuffer) {

    DataByteBuffer dataByteBuffer;

    try {
      dataByteBuffer = new DataByteBuffer(byteBuffer);
      String url = dataByteBuffer.getUrl();

      System.out.println(url);

      switch (url) {
      case "/user/reg":
        registerUser(socketChannel, (Accounts) dataByteBuffer.getData());
        break;

      case "/user/log":
        break;

      default:

      }
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }

  private static void registerUser(SocketChannel socketChannel, Accounts account) {
    ResultSet resultSet;
    String nickname = account.getNickname();
    String password = account.getPassword();
    String sql;
    try {
      sql = "select count(nickname) as count from accounts where nickname=\'" + nickname + "\'";

      resultSet = dbConnection.query(sql);
      resultSet.next();

      // 该账户是否已经被占用
      int num = resultSet.getInt("count");

      if (num != 0) {
        socketChannel.write(Message.encode("不能使用这个昵称"));
        return;
      }
      sql = "insert into accounts (nickname, password) values('" + nickname + "\', \'" + password + "')";
      dbConnection.update(sql);
      sql = "select id, nickname from accounts where nickname='" + nickname + "'";
      resultSet = dbConnection.query(sql);

      resultSet.next();

      int id = resultSet.getInt("id");

      String message = "注册成功：" + id + ":" + nickname;

      socketChannel.write(Message.encode(message));
    } catch (IOException | SQLException ioE) {
      ioE.printStackTrace();
    }
  }
}