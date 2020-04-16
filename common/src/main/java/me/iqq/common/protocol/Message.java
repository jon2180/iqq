package me.iqq.common.protocol;

import java.io.Serializable;

/**
 * 描述 消息表 的 数据库表字段类型
 * 类名与数据库字段名一致。类中每一个属性都对应一个数据表的字段。实现 Serializable 是为了可以在网络流中传输对象。
 *
 * @version v191208
 */
public class Message implements Serializable {
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
    private String originAccount;

    /**
     * 收消息的账号
     */
    private String targetAccount;

    /**
     * 消息内容，之后可能要该
     */
    private String content;

    private long time;
    /**
     * 消息类型，可能是文本，图片，音频，视频等，但具体实现之后考虑
     */
    private int type;

    public Message(String qq, String friendQQ, String content, int type) {
//        this.id = id;
        this.originAccount = qq;
        this.targetAccount = friendQQ;
        this.content = content;
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

    public long getTime() {
        return time;
    }

    /**
     * @return the origin_account
     */
    public String getOriginAccount() {
        return originAccount;
    }

    /**
     * @param originAccount the origin_account to set
     */
    public void setOriginAccount(String originAccount) {
        this.originAccount = originAccount;
    }

    /**
     * @return the target_account
     */
    public String getTargetAccount() {
        return targetAccount;
    }

    /**
     * @param targetAccount the target_account to set
     */
    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
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
