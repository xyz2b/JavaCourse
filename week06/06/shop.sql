CREATE TABLE IF NOT EXISTS `customer` (
	`customer_id` 		  	  INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '用户表自增主键ID',
	`customer_name`     	  VARCHAR(20) NOT NULL COMMENT '用户名',
	`customer_nick_name`	  VARCHAR(20) DEFAULT NULL COMMENT '用户昵称',
  `user_money`            DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '用户余额',
	`register_date`			    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT	'注册时间',
  `modified_time`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
	`is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
	PRIMARY KEY (`user_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户表';

CREATE TABLE IF NOT EXISTS `address` (
	`customer_addr_id`      INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '地址表自增主键ID',
	`customer_id`			      INT UNSIGNED NOT NULL COMMENT 'customer表的自增ID',
  `zip`					          SMALLINT NOT NULL COMMENT '邮编',
	`province`			      	VARCHAR(20) NOT NULL COMMENT '省份',
	`city`					        VARCHAR(20) NOT NULL COMMENT '城市',
	`district`				      VARCHAR(20) NOT NULL COMMENT '区',
	`address`				        VARCHAR(200) NOT NULL COMMENT '具体地址',
	`is_default`			      TINYINT NOT NULL COMMENT '是否默认',
	`modified_time`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
	`create_date`		  	    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
  PRIMARY KEY pk_customeraddid(customer_addr_id)
)ENGINE=innodb DEFAULT CHARSET=utf8 COMMENT '用户地址表';

CREATE TABLE IF NOT EXISTS `product` (
	`product_id`			      INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '商品ID',
  `product_core`          CHAR(16) NOT NULL COMMENT '商品编码',
	`product_name`			    VARCHAR(100) NOT NULL COMMENT '商品名称',
	`product_price`			    INT UNSIGNED NOT NULL COMMENT '商品单价',
	`product_descript`			TEXT DEFAULT NULL COMMENT '商品描述',
	`product_category`		  VARCHAR(10) DEFAULT 'default' COMMENT '商品分类',
  `publish_status`        TINYINT NOT NULL DEFAULT 0 COMMENT '上下架状态：0下架 1上架',
	`create_date`			      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '商品录入时间',
	`modified_date`		      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	`is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
	PRIMARY KEY (`commodity_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '商品信息表';


CREATE TABLE IF NOT EXISTS `order` (
	`order_id`				      INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '订单ID',
	`order_sn`				      BIGINT UNSIGNED NOT NULL COMMENT '订单编号 yyyymmddnnnnnnnn',
	`user_id`   			      INT UNSIGNED NOT NULL COMMENT '下单用户ID',
  `address`               INT UNSIGNED NOT NULL COMMENT '收货地址信息，用户地址表的自增主键ID',
	`payment_method`	      TINYINT NOT NULL COMMENT '支付方式：1现金，2余额，3网银，4支付宝，5微信',
	`order_money` 			    DECIMAL(8,2) NOT NULL COMMENT '订单金额',
  `district_money`		    DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
  `shipping_money`  		  DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '运费金额',
  `payment_money`	  		  DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '支付金额',
	`pay_date`			    	  TIMESTAMP DEFAULT NULL COMMENT '支付时间',
	`shipping_date`			    TIMESTAMP DEFAULT NULL COMMENT '发货时间',
	`receive_time`		    	TIMESTAMP DEFAULT NULL COMMENT '收货时间',
	`order_status`			    TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态',
	`create_date`			      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
	`update_date`			      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP '最后更新时间',
	`is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
	PRIMARY KEY (`order_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '订单主表';

CREATE TABLE IF NOT EXISTS `order_detail` (
	`order_detail_id` 	  	INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
	`order_id` 				      INT UNSIGNED NOT NULL COMMENT '订单ID',
	`product_id`			      INT UNSIGNED NOT NULL COMMENT '订单商品ID',
	`product_name`		    	VARCHAR(50) NOT NULL COMMENT '商品名称',
	`product_cnt`		      	INT NOT NULL DEFAULT 1 COMMENT '购买商品数量',
  `product_price`	    		DECIMAL(8,2) NOT NULL COMMENT '购买商品单价',
  `is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
  `modified_time`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY pk_orderdetailid(order_detail_id)
)ENGINE=innodb DEFAULT CHARSET=utf8 COMMENT '订单详情表';