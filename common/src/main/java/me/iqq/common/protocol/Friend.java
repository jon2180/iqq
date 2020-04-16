package me.iqq.common.protocol;

import java.io.Serializable;

/**
 * 朋友关系表的字段映射
 *
 * <p>类名与数据库字段名一致。类中每一个属性都对应一个数据表的字段。实现 Serializable 是为了可以在网络流中传输对象。</p>
 *
 * @version v191208
 */
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 独一无二的 message id
     */
    private int id;

    /**
     * 发送者账户
     */
    private String originUserId;

    /**
     * 接收者账户
     */
    private String targetUserId;

    /**
     * 所在群组
     */
    private String groupName;

    /**
     * 好友状态，即为是否在黑名单
     */
    private int type;


    public Friend(int id, String QQ, String friendQQ, String groupName, int type) {
        this.id = id;
        this.originUserId = QQ;
        this.targetUserId = friendQQ;
        this.groupName = groupName;
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
    public String getOriginUserId() {
        return originUserId;
    }

    /**
     * @param originUserId the origin_account to set
     */
    public void setOriginUserId(String originUserId) {
        this.originUserId = originUserId;
    }

    /**
     * @return the target_account
     */
    public String getTargetUserId() {
        return targetUserId;
    }

    /**
     * @param targetUserId the target_account to set
     */
    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    /**
     * @return the group_name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the group_name to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
