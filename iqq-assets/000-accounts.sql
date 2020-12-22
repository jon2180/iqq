create table iqq_user
(
    id        varchar(16) comment '用户唯一id， 即 QQ 号码' primary key,
    nickname  varchar(64)       not null comment '用户昵称',
    avatar    varchar(256)      null comment '用户头像的Url',
    signature varchar(140)      null comment '签名',
    tel       varchar(16)       null comment '电话号码',
    email     varchar(128)      null comment '用户的邮箱',
    password  varchar(128)      null comment '用户密码',
    salt      varchar(64)       null comment '密码盐,通过盐与用户输入密码组合然后md5加密形成密码',
    ip        varchar(64)       null comment '最近一次登录ip address',
    birthday  date              null comment '生日',
    status    tinyint default 0 null comment '用户账号状态'
) comment '用户账户表';

