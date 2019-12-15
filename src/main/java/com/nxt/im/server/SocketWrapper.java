package com.nxt.im.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.nxt.im.common.Friends;
import com.nxt.im.db.DatabaseConnection;

public class SocketWrapper {
  /**
   * 用户id
   */
  private String userId;

  /**
   * 与用户相绑定的通道
   */
  private SocketChannel channel;

  public SocketWrapper(String userId, SocketChannel channel) {
    this.userId = userId;
    this.channel = channel;
  }

  /**
   * @return the userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * @return the channel
   */
  public SocketChannel getChannel() {
    return channel;
  }

  /**
   * 获取好友列表
   * 
   * @return the fridents
   */
  public static Vector<Friends> getAllFridents(String qq) {
    Vector<Friends> friends = new Vector<Friends>();

    DatabaseConnection dbConn = new DatabaseConnection();

    String sql = "select id, target_account, group_name, type from friends where origin_account='" + qq + "'";

    try {
      ResultSet res = dbConn.query(sql);
      while (res.next()) {
        int friendId = res.getInt("id");
        String targetAccount = res.getString("target_account");
        String groupName = res.getString("group_name");
        int type = res.getInt("type");

        friends.add(new Friends(friendId, qq, targetAccount, groupName, type));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    } finally {
      dbConn.close();
    }

    // TODO 测试一下结果
    Iterator<Friends> it = friends.iterator();
    while (it.hasNext()) {
      System.out.println(it.next().getTarget_account());
    }

    return friends;
  }

  /**
   * 通知所有好友，xxx上线了
   * 
   * @param qq
   */
  public void notifyAllFriends(String qq) {
    Vector<Friends> friends = getAllFridents(qq);
    HashMap<String, SocketWrapper> map = NioServer.getSocketMap();
    Friends friend;
    Iterator<Friends> it = friends.iterator();
    while (it.hasNext()) {
      friend = it.next();
      if (map.containsKey(friend.getTarget_account())) {
        try {
          map.get(friend.getTarget_account()).getChannel().write(Message.encode(qq + "上线了"));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}