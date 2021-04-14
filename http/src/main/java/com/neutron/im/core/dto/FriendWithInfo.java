package com.neutron.im.core.dto;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class FriendWithInfo {
    /**
     * 独一无二的 message id
     */
    private String id;

    private String account_id;

    /**
     * 昵称
     */
    private String nickname;
    private String remark_name;

    /**
     * 签名
     */
    private String signature;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 所在群组
     */
    private String category;

    private Timestamp link_time;

    /**
     * 好友关系状态
     */
    private int status;
    /**
     * 好友状态，即为是否在黑名单
     */
    private int type;
}
