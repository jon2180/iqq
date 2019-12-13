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
    case "/user/login":
      loginCheck(socketChannel, (Accounts) dataByteBuffer.getData());
    }

    // Accounts account = (Accounts) dataByteBuffer.getData();
    // System.out.println(account.getSignature());
  }

  // 用户注册
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

  /**
   * 触发登录验证
   *
   * TODO: 当客户端将登录验证所需要的信息包装成一个Accounts对象，此时，在这里创建一个Login对象，
   *       该对象中包含有登录验证的方法，那么，socketChannel是用来做什么呢？
   * @version 191213
   */
  private static void loginCheck(SocketChannel socketChannel, Accounts account) {
    String nickname = account.getNickname();
    String password = account.getPassword();

    Login login = new Login(nickname,password);
    if(login.check() == true){
      System.out.println("登录验证成功！");


      /**
       * TODO: 这个时候要对socketChannel做点啥？是不？
       * 类似于session记录登录状态的感觉？
       * 把nickname放到某个已登录队列里去的这种操作？
       */

       
    }else{
      System.out.println("登录验证失败！");
    }


    
  }
}