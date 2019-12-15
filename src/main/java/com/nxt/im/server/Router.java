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
 * @version 191215
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
        registerUser(socketChannel, (Accounts) dataByteBuffer.getData());
        break;
      case "/user/login":
        loginCheck(socketChannel, (Accounts) dataByteBuffer.getData());
        break;
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

      // 随机出来一个QQ号
      String qq;

      boolean isRepeat;

      do {
        qq = Message.getRandom();

        sql = "select qnumber, nickname from accounts where qnumber=\'" + qq + "\'";

        // 判断QQ号是否已经被占用
        isRepeat = dbConnection.query(sql).next();
      } while (isRepeat);

      sql = "insert into accounts (qnumber, nickname, password) values('" + qq + "','" + nickname + "', '" + password
          + "')";
      dbConnection.update(sql);

      // TODO 待把客户端接收部分处理好了再取消注释
      // 把账户返回给客户端
      // account.setQnumber(qq);
      // socketChannel.write(new DataByteBuffer("re:/user/reg",
      // account).toByteBuffer());

      String message = "注册成功：" + qq + ":" + nickname;

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

      // x 账户不存在
      if (resultSet.next()) {
        System.out.println("无此用户");
        String msg = "10002";
        socketChannel.write(Message.encode(msg));
        return;
      }

      String realPassword = resultSet.getString("password");

      System.out.println("password:" + password);
      System.out.println("realPassword:" + realPassword);

      // x 登录账户与密码不匹配
      if (realPassword.equals(password)) {
        System.out.println("登录失败，密码错误");
        return;
      }

      // v 正常登录
      System.out.println("成功登录");

      String msg = "10000";
      socketChannel.write(Message.encode(msg));

      // 把当前用户的 qq号 作为键，对应的 SocketWrapper 对象作为值，放进 socketMap 中
      // SocketWrapper socketWrapper = new SocketWrapper(qnumber, socketChannel);
      // NioServer.getSocketMap().put(qnumber, socketWrapper);

      // TODO 获取到用户的好友列表
      // select id, origin_account, target_account, group_name, type from friends
      // where origin_account= + id

      // TODO 获取各好友的在线状态，并通知在线好友：“我上线了”
      // Iterator<String> it = socketWrapper.getAllFridents().iterator();
      // while (it.hasNext()) {
        // String friendId = it.next();
        // if (NioServer.getSocketMap().containsKey(friendId)) {
          // try { 
            // TODO 消息需要包装，这里暂时还没有进行包装
            // NioServer.getSocketMap().get(friendId).getChannel().write(Message.encode(friendId + "上线了"));
          // } catch (IOException e) {
            // e.printStackTrace();
          // }
        // }
      // }
    } catch (IOException | SQLException ioE) {
      ioE.printStackTrace();
    }
  }
}