package com.nxt.im.server;

import java.io.IOException;
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

  public static void dispatch(SocketChannel socketChannel, DataByteBuffer dataByteBuffer) {
    String url = dataByteBuffer.getUrl();

    System.out.println(url);

    switch (url) {
    case "/user/reg":
      registerUser(socketChannel, (Accounts) dataByteBuffer.getData());
    }

    // Accounts account = (Accounts) dataByteBuffer.getData();
    // System.out.println(account.getSignature());
  }

  private static void registerUser(SocketChannel socketChannel, Accounts account) {

    String sql = "select count(nickname) as count from accounts where nickname=\'" + account.getNickname() + "\'";

    ResultSet resultSet = dbConnection.query(sql);

    try {
      // if (resultSet.next()) {
      resultSet.next();
      int num = resultSet.getInt("count");
      System.out.print(num);
      // }
      if (num > 0) {
        System.out.println("不能使用这个昵称");
        // try {
        socketChannel.write(Message.encode("不能使用这个昵称"));
        // }
      } else {
        String password = account.getPassword();

        

        sql = "insert into accounts (nickname, signature, )";
      }
      // while (resultSet.next()) {
      // int id = resultSet.getInt("id");
      // String nickname = resultSet.getString("nickname");
      // String signature = resultSet.getString("signature");

      // System.out.println(id + ":" + nickname + ":" + signature);
      // }
    } catch (IOException ioE) {
      ioE.printStackTrace();
      return;
    } catch (SQLException sqlE) {
      sqlE.printStackTrace();
    }

    // System.out.println("注册成功");
    System.out.println(account.getSignature());
  }
}