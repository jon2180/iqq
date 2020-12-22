package me.iqq.server.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 朋友关系表的字段映射
 *
 * <p>类名与数据库字段名一致。类中每一个属性都对应一个数据表的字段。实现 Serializable 是为了可以在网络流中传输对象。</p>
 *
 * @version v191208
 */
@Data
@Deprecated
public class Friends implements Serializable {

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


    public Friends(int id, String QQ, String friendQQ, String groupName, int type) {
        this.id = id;
        this.originUserId = QQ;
        this.targetUserId = friendQQ;
        this.groupName = groupName;
        this.type = type;
    }

    public Friends() {
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Friends)) return false;
        final Friends other = (Friends) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$originUserId = this.getOriginUserId();
        final Object other$originUserId = other.getOriginUserId();
        if (this$originUserId == null ? other$originUserId != null : !this$originUserId.equals(other$originUserId))
            return false;
        final Object this$targetUserId = this.getTargetUserId();
        final Object other$targetUserId = other.getTargetUserId();
        if (this$targetUserId == null ? other$targetUserId != null : !this$targetUserId.equals(other$targetUserId))
            return false;
        final Object this$groupName = this.getGroupName();
        final Object other$groupName = other.getGroupName();
        if (this$groupName == null ? other$groupName != null : !this$groupName.equals(other$groupName)) return false;
        if (this.getType() != other.getType()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Friends;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $originUserId = this.getOriginUserId();
        result = result * PRIME + ($originUserId == null ? 43 : $originUserId.hashCode());
        final Object $targetUserId = this.getTargetUserId();
        result = result * PRIME + ($targetUserId == null ? 43 : $targetUserId.hashCode());
        final Object $groupName = this.getGroupName();
        result = result * PRIME + ($groupName == null ? 43 : $groupName.hashCode());
        result = result * PRIME + this.getType();
        return result;
    }

    public String toString() {
        return "Friend(id=" + this.getId() + ", originUserId=" + this.getOriginUserId() + ", targetUserId=" + this.getTargetUserId() + ", groupName=" + this.getGroupName() + ", type=" + this.getType() + ")";
    }
}
