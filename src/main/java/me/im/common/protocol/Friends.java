package me.im.common.protocol;

import java.io.Serializable;

/**
 * 朋友关系表的字段映射
 *
 * <p>类名与数据库字段名一致。类中每一个属性都对应一个数据表的字段。实现 Serializable 是为了可以在网络流中传输对象。</p>
 *
 * @version v191208
 */
public class Friends implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 独一无二的id
     */
    private int id;

    /**
     * 当前账户
     */
    private String origin_account;

    /**
     * 好友账户
     */
    private String target_account;

    /**
     * 所在群组
     */
    private String group_name;

    /**
     * 好友状态，即为是否在黑名单
     */
    private int type;


    public Friends(int id, String QQ, String friendQQ, String groupName, int type) {
        this.id = id;
        this.origin_account = QQ;
        this.target_account = friendQQ;
        this.group_name = groupName;
        this.type = type;
    }

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
    public String getOrigin_account() {
        return origin_account;
    }

    /**
     * @param origin_account the origin_account to set
     */
    public void setOrigin_account(String origin_account) {
        this.origin_account = origin_account;
    }

    /**
     * @return the target_account
     */
    public String getTarget_account() {
        return target_account;
    }

    /**
     * @param target_account the target_account to set
     */
    public void setTarget_account(String target_account) {
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
