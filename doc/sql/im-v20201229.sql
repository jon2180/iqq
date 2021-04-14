create table accounts
(
    id varchar(10) not null comment '用户唯一id， 即 QQ 号码'
        primary key,
    nickname varchar(64) not null comment '用户昵称',
    avatar varchar(256) null comment '用户头像的Url',
    signature varchar(140) null comment '签名',
    phone varchar(16) null comment '电话号码',
    email varchar(128) not null comment '用户的邮箱',
    password varchar(128) not null comment '用户密码',
    salt varchar(64) not null comment '密码盐,通过盐与用户输入密码组合然后md5加密形成密码',
    login_ip varchar(64) null comment '最近一次登录ip address',
    birthday timestamp null comment '生日',
    register_time timestamp default CURRENT_TIMESTAMP null comment '注册时间',
    status tinyint default 0 null comment '用户账号状态'
)
    comment '用户账户表';

create table friends
(
    id int unsigned auto_increment comment '用户唯一id'
        primary key,
    first_account varchar(10) not null comment '源账号',
    second_account varchar(10) not null comment '目标账号',
    group_name_for_first varchar(32) null comment '所在组名',
    group_name_for_second varchar(32) null comment '所在组名',
    link_time timestamp default CURRENT_TIMESTAMP null comment '添加好友时间',
    type int unsigned default '0' null comment '好友类型，xxx 第一位 是否是好友，第二位，是否在黑名单，第三位，是否是特别关注，正常为0，否则为1'
)
    comment '朋友关系表';

create table messages
(
    id int unsigned auto_increment comment '用户唯一id'
        primary key,
    sender varchar(10) not null comment '源账号',
    receiver varchar(10) not null comment '目标账号',
    type int unsigned not null comment '消息类型 xxx 第一位 字符串 / 二进制 第二三位 如果是二进制，是二进制图片，二进制语音，二进制视频，二进制xx',
    content text null comment '消息内容',
    time timestamp default CURRENT_TIMESTAMP null comment '消息发送时间'
)
    comment '消息内容表';

