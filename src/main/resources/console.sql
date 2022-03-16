CREATE database if NOT EXISTS `seckill` default character set utf8mb4 collate utf8mb4_unicode_ci;
use seckill;
create table t_user(
    `id` bigint(20) not null comment '用户id，手机号码',
    `nickname` VARCHAR(32) not null ,
    `password` varchar(32) default null comment 'MD5(MD5(pass明文+固定salt)+salt',
    `head` varchar(128) default null comment '头像',
    `register_date` datetime null DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `last_login_date` datetime default null comment '最后一次登录时间',
    `login_count` int(11) default '0' comment '登录次数',
    primary key (`id`)
);
create table t_goods(
    `id` bigint(20) not null auto_increment comment '商品id',
    `goods_name` varchar(16) default null   comment '商品名称',
    `goods_title` varchar(64) default null  comment '商品标题',
    `goods_img` varchar(64) default null comment '商品图片',
    `goods_detail` longtext comment '商品详情',
    `goods_price` decimal(10,2) default '0.00' comment '商品价格',
    `goods_stock` int(11) default '0' comment '商品库存,-1表示无限制',
    primary key (`id`)
)engine =INNODB auto_increment=3 default charset=utf8mb4;
create table t_order(
    `id` bigint(20) not null AUTO_INCREMENT comment '订单id',
    `user_id` bigint(20) default null   comment  '用户id',
    `goods_id` bigint(20) default null comment '商品id',
    `delivery_addr_id` bigint(20) default null comment '收货地址id',
    `goods_name` varchar(16) default null comment '商品名字',
    `goods_count` int(11) default '0' comment '商品数量',
    `goods_price` decimal(10,2) default '0.00' comment '商品价格',
    `order_channel` tinyint(4) default '0' comment '下单端口，1pc,2android,3ios',
    `status` tinyint(4) default '0' comment '订单状态,0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
    `create_date` datetime default null comment '订单创建时间',
    `pay_date` datetime default null comment  '订单支付时间',
    primary key (`id`)
)engine =INNODB auto_increment=3 default charset=utf8mb4;
create table t_seckill_goods(
    `id` bigint(20) not null auto_increment comment '秒杀商品id',
    `goods_id` bigint(20) default null comment '商品id',
    `seckill_price` decimal(10,2) default '0.00' comment '秒杀价格',
    `stock_count` int(11) default '0' comment '秒杀库存',
    `start_time` datetime default null  comment '秒杀开始时间',
    `end_time` datetime default null comment '秒杀结束时间',
    primary key (`id`)
)engine =INNODB auto_increment=3 default charset=utf8mb4;
create table t_seckill_order(
    `id` bigint(20) not null auto_increment comment '秒杀订单id',
    `goods_id` bigint(20) default null comment '商品id',
    `user_id` bigint(20) default null comment '用户id',
    `order_id` bigint(20)  default null comment '订单id',
    primary key (`id`)
)engine =INNODB auto_increment=3 default charset=utf8mb4;
insert into t_goods values (1,'iPhone 13 128GB','iPhone 13 128GB','/img/iphone13.jpg','苹果13','5999','100'),
(2,'iPad Pro 128GB','iPad Pro 128GB','/img/iPad Pro.jpg','iPad Pro','6499','50'),
(3,'MacBook Pro','MacBook Pro','/img/MacBook Pro.jpg','MacBook Pro','14999','20');
insert into t_seckill_goods values (1,1,'4999',5,'2022-03-03 08:00:00','2022-03-03 09:00:00'),
                                   (2,2,'5999',5,'2022-03-03 08:00:00','2022-03-03 09:00:00'),
                                   (3,3,'13999',5,'2022-03-03 08:00:00','2022-03-03 09:00:00');
insert into t_user values (13256773444,'admin','171afff6236c1776b1896e89326a50d2',null,'2022-03-07 16:38:14',null,1)



