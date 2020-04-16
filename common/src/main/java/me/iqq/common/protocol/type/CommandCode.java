package me.iqq.common.protocol.type;

public enum CommandCode {
//    public static final int LOGIN = 1000; // 登陆
//    public static final int LOGOUT = 1001; // 下线
//    public static final int BUYS = 1002;
//    public static final int LEAVE = 1003;
//    public static final int SEND = 1004; // 发送消息
//    public static final int DOUDONG = 1005; // 抖动
//    public static final int ADDFRIEND = 1006; // 添加好友
//    public static final int DELFRIEND = 1007; // 删除好友

    ADD_FRIEND //    public static final String
    , // = "/friend/add";
    DELETE_FRIEND //    public static final String
    , // = "/friend/remove";
    GET_FRIEND_LIST //    public static final String
    ,
    LOG_IN, // = "/user/log";
    LOG_OUT, //= "/user/log_out";
    NOTIFY_ONLINE //    public static final String
    , // = "/friend/log";
    REG, // = "/user/reg";
    SEND_MESSAGE // = "/msg/to";
    ; //= "/friend/list";
}
