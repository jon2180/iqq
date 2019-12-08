package com.nxt.im.db;

import java.io.Serializable;

/**
 * 描述 消息表 的 数据库表字段类型
 * 
 * @Description 类名与数据库字段名一致。类中每一个属性都对应一个数据表的字段。实现 Serializable 是为了可以在网络流中传输对象。
 * @version v191208
 */
public class Messages implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   * 消息id
   */
  private int id;
  /**
   * 发消息的账号
   */
  private int origin_account;

  /**
   * 收消息的账号
   */
  private int target_account;

  /**
   * 消息内容，之后可能要该
   */
  private String content;

  /**
   * 消息类型，可能是文本，图片，音频，视频等，但具体实现之后考虑
   */
  private int type;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the origin_account
   */
  public int getOrigin_account() {
    return origin_account;
  }

  /**
   * @param origin_account the origin_account to set
   */
  public void setOrigin_account(int origin_account) {
    this.origin_account = origin_account;
  }

  /**
   * @return the target_account
   */
  public int getTarget_account() {
    return target_account;
  }

  /**
   * @param target_account the target_account to set
   */
  public void setTarget_account(int target_account) {
    this.target_account = target_account;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the type
   */
  public int getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(int type) {
    this.type = type;
  }
}