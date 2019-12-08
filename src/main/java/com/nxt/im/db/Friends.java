package com.nxt.im.db;

import java.io.Serializable;

/**
 * 朋友关系表的字段映射
 * 
 * @Description 类名与数据库字段名一致。类中每一个属性都对应一个数据表的字段。实现 Serializable 是为了可以在网络流中传输对象。
 * @version v191208
 */
public class Friends implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   * 独一无二的id
   */
  private int id;

  /**
   * 当前账户
   */
  private int origin_account;

  /**
   * 好友账户
   */
  private int target_account;

  /**
   * 所在群组
   */
  private String group_name;

  /**
   * 好友状态，即为是否在黑名单
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
   * @return the group_name
   */
  public String getGroup_name() {
    return group_name;
  }

  /**
   * @param group_name the group_name to set
   */
  public void setGroup_name(String group_name) {
    this.group_name = group_name;
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