package com.nxt.im.server;

import java.nio.channels.SocketChannel;
import java.util.Vector;

public class SocketWrapper {
  /**
   * 用户id
   */
  private int userId;

  /**
   * 与用户相绑定的通道
   */
  private SocketChannel channel;

  private Vector<Integer> fridents;

  public SocketWrapper(int userId, SocketChannel channel) {
    this.userId = userId;
    this.channel = channel;
  }

  /**
   * @return the userId
   */
  public int getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(int userId) {
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
  public void setFridents(Vector<Integer> fridents) {
    this.fridents = fridents;
  }

  /**
   * @return the fridents
   */
  public Vector<Integer> getFridents() {
    return fridents;
  }
}