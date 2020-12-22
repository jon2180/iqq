create table friends
(
    id             int(11) unsigned auto_increment comment '用户唯一id' primary key,
    origin_account varchar(10)  not null comment '源账号',
    target_account varchar(10)  not null comment '目标账号',
    group_name     varchar(32)  null comment '所在组名',
    time           int unsigned not null comment '添加好友时间',
    type           int unsigned not null comment '好友类型，正常为0，异常为1'
) comment '朋友关系表';

