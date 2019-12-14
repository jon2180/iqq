package com.nxt.im.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

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
      switch (url) {
      case "/user/reg":
        registerUser(socketChannel, (Accounts) dataByteBuffer.getData());break;
      case "/user/login":
        loginCheck(socketChannel, (Accounts) dataByteBuffer.getData());break;
      }
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 注册用户
   * 
   * @param socketChannel 用来给客户端回复消息
   * @param account       需要的数据
   */
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

  /**
   * 触发登录验证
   *
   * TODO: 当客户端将登录验证所需要的信息包装成一个Accounts对象，此时，在这里创建一个Login对象，
   * 该对象中包含有登录验证的方法，那么，socketChannel是用来做什么呢？
   * 
   * @version 191213
   */
  private static void loginCheck(SocketChannel socketChannel, Accounts account) {
    ResultSet resultSet;
    String qnumber = account.getQnumber();
    String password = account.getPassword();
    String sql;
    try {
      sql = "SELECT id,qnumber,nickname,password FROM accounts where qnumber = '" + qnumber + "'";
      resultSet = dbConnection.query(sql);

      if(resultSet.next() != false){
        String realPassword = resultSet.getString("password");

        System.out.println("password:" + password);
        System.out.println("realPassword:" + realPassword);


        if(realPassword.equals(password)){
          System.out.println("成功登录");
          String msg = "10000";
          socketChannel.write(Message.encode(msg));
        }else{
          System.out.println("登录失败");
        }
      }else{
        System.out.println("无此用户");
        String msg = "10002";
        socketChannel.write(Message.encode(msg));
      }
    } catch (IOException | SQLException ioE) {
      ioE.printStackTrace();
    }
    

    






    // Login login = new Login(qnumber, password);
    // if (login.check() == true) {
      // System.out.println("登录验证成功！");

      /**
       * TODO: 这个时候要对socketChannel做点啥？是不？ 类似于session记录登录状态的感觉？
       * 把nickname放到某个已登录队列里去的这种操作？
       */
      // 假设 id 为 2
      Integer id = 5;
      SocketWrapper socketWrapper = new SocketWrapper(id, socketChannel);
      
      // TODO 把当前用户的 id 作为键，对应的 SocketWrapper 对象作为值，放进 socketMap 中
      // NioServer.getSocketMap().put(id, socketWrapper);

      // TODO 获取到用户的好友列表
      // select id, origin_account, target_account, group_name, type from friends where origin_account= + id

      // TODO 获取各好友的在线状态，并通知在线好友：“我上线了”
      /*
      Iterator<Integer> it = socketWrapper.getFridents().iterator();
      while(it.hasNext()) {
        // TODO 通知其在线好友，xxx 上线了
        Integer friendId = it.next();
        if (NioServer.getSocketMap().containsKey(friendId)) {
          try {
            // TODO 消息可能需要包装，这里没有进行包装
            NioServer.getSocketMap().get(friendId).getChannel().write(Message.encode(friendId + "上线了"));
          } catch(IOException e) {
            e.printStackTrace();
          }
        }
      }
      */
      /*
      try {
        // TODO 向用户返回消息：好友列表，和在线状态，以及一些未接收消息
        socketChannel.write();
      } catch(IOException e) {
        
      }
      */


    // } else {
    //   System.out.println("登录验证失败！");
    // }

  }
}