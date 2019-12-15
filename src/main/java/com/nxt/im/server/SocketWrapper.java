package com.nxt.im.server;

import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
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

  private Vector<Friends> fridents;

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
   * @param fridents the fridents to set
   */
  public void setFridents(Vector<Friends> fridents) {
    this.fridents = fridents;
  }

  /**
   * @return the fridents
   */
  public Vector<Friends> getAllFridents() {
    return fridents;
  }

  public void updateFriendsList() {
    // DatabaseConnection dbConn = new DatabaseConnection();

    // Vector<String> temp = new Vector<String>(); 

    // String sql = "select target_account, group_name, type from friends where origin_account=" + userId;

    // ResultSet resultSet;
    // try {
    //   resultSet = dbConn.query(sql);
    //   while(resultSet.next()) {
    //     Friends friends = new Friends();
    //     friends.setId();
    //     int targetAccount = resultSet.getInt("target_account");
    //     String groupName = resultSet.getString("group_name");


    //   }
    // } catch (SQLException e) {
    //   e.printStackTrace();
    // }
  }

  public void notifyAllFriends() {

  }
}