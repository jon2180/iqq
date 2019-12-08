-- 账号表
create table accounts (
  `id` int(11) unsigned not null auto_increment comment '用户唯一id',
  `nickname` varchar(64) not null comment '用户昵称',
  `signature` varchar(140) comment '签名',
  `tel` varchar(16) comment '电话号码',
  `ip` varchar(64) comment 'ip address',
  `port` int comment 'port',
  `avatar` varchar(256) comment '用户头像的Url',
  `email` varchar(128) unique comment '用户的邮箱',
  `password` varchar(128) comment '用户密码',
  `salt`varchar(64) comment '密码盐,通过盐与用户输入密码组合然后md5加密形成密码',
  `birthday` date comment '生日',
  `status` tinyint(4)  default 0 comment '用户账号状态',
  primary key(`id`)
) engine = innodb default charset=UTF8MB4 comment='用户账户表';


-- 消息内容表

create table messages (
  `id` int(11) unsigned not null auto_increment comment '用户唯一id',
  `origin_account` int(11) unsigned  not null comment '源账号',
  `target_account` int(11) unsigned not null comment '目标账号',
  `type` int unsigned not null comment '消息类型',
  `content` text comment '消息内容',
  primary key(`id`)
) engine = innodb default charset=UTF8MB4 comment='消息内容表';

-- 朋友关系表
create table friends (
  `id` int(11) unsigned not null auto_increment comment '用户唯一id',
  `origin_account` int(11) unsigned  not null comment '源账号',
  `target_account` int(11) unsigned not null comment '目标账号',
  `group_name` varchar(32) comment '所在组名',
  `type` int unsigned not null comment '好友类型，正常为0，异常为1',
  primary key(`id`)
) engine = innodb default charset=UTF8MB4 comment='朋友关系表';