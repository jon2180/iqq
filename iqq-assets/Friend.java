package me.iqq.common.protocol;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 朋友关系表的字段映射
 *
 * <p>类名与数据库字段名一致。类中每一个属性都对应一个数据表的字段。实现 Serializable 是为了可以在网络流中传输对象。</p>
 *
 * @version v191208
 */
@Deprecated
//@Data
@Builder
//@NoArgsConstructor
@Getter
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
}
