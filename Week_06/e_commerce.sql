-- user
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `user_id`     bigint             NOT NULL AUTO_INCREMENT COMMENT 'ID,数据库中的ID',
    `user_name`   varchar(20) UNIQUE NOT NULL COMMENT '用户name,用户定义一个唯一的昵称作为自己登录的名称',
    `user_psw`    varchar(256)       NOT NULL COMMENT '存储密码加密以后的值',
    `user_gender` tinyint            NOT NULL COMMENT '0表示男,1表示女',
    `user_icon`   varchar(256) COMMENT '用户头像的URL地址',
    `user_phone`  varchar(20)        NOT NULL COMMENT '用户的手机号',
    `user_email`  varchar(30) COMMENT '用户的邮箱',
    `vip_grade`   tinyint            NOT NULL DEFAULT 0 COMMENT 'vip等级',
    `create_time` bigint             NOT NULL COMMENT '创建时间',
    `update_time` bigint             NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC COMMENT '用户表';

-- user_addresss
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`
(
    `user_id`      bigint       NOT NULL COMMENT '用户ID',
    `address_id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '用户地址的id，用户的地址不止一个',
    `address_name` varchar(256) NOT NULL COMMENT '用户地址',
    `create_time`  bigint       NOT NULL COMMENT '创建时间',
    `update_time`  bigint       NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`address_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC COMMENT '用户的地址表';

-- merchandise
DROP TABLE IF EXISTS `merchandise`;
CREATE TABLE `merchandise`
(
    `merchandise_id`    bigint      NOT NULL AUTO_INCREMENT COMMENT '商品id',
    `merchandise_type`  varchar(20) NOT NULL COMMENT '商品类型',
    `merchandise_name`  varchar(20) NOT NULL COMMENT '商品名称',
    `merchandise_nums`  int         NOT NULL COMMENT '商品数量',
    `merchandise_price` decimal     NOT NULL COMMENT '商品价格',
    `currency_type`     tinyint     NOT NULL COMMENT '货币类型',
    `create_time`       bigint      NOT NULL COMMENT '创建时间',
    `update_time`       bigint      NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`merchandise_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC COMMENT '商品表';

-- use_order
DROP TABLE IF EXISTS `use_order`;
CREATE TABLE `user_order`
(
    `order_id`       bigint  NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id`        bigint  NOT NULL COMMENT '用户ID',
    `merchandise_id` bigint  NOT NULL COMMENT '商品id',
    `purchase_num`   int     NOT NULL COMMENT '商品购买数量',
    `address_id`     bigint  NOT NULL COMMENT '用户的地址id',
    `total_prices`   decimal NOT NULL COMMENT '订单的总价',
    `currency_type`  tinyint NOT NULL COMMENT '货币类型',
    `order_state`    tinyint NOT NULL COMMENT '订单的支付状态',
    PRIMARY KEY (`order_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC COMMENT '订单表';
