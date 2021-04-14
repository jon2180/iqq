package com.neutron.im.core.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChatWithInfo {
    private String id;
    /**
     * 我的账户 id
     */
    private String account_id;
    /**
     * 对应的好友信息在好友表的唯一 id
     */
    private String fid;
    /**
     * 朋友的账户 id
     */
    private String friend_id;
    private String target_id;

    /**
     * 朋友的昵称
     */
    private String nickname;
    /**
     * 朋友的头像
     */
    private String avatar;
    /**
     * 朋友的签名
     */
    private String signature;
    /**
     * 对朋友的备注名
     */
    private String remark_name;
}
