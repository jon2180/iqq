create table messages
(
    id             int(11) unsigned auto_increment comment '用户唯一id' primary key,
    origin_account varchar(10)  not null comment '源账号',
    target_account varchar(10)  not null comment '目标账号',
    type           int unsigned not null comment '消息类型',
    content        text         null comment '消息内容',
    time           int(10)      null comment '消息发送时间'
) comment '消息内容表';

