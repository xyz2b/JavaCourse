CREATE TABLE IF NOT EXISTS `t_customer` (
	`f_customer_id` 		  	  INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '用户表自增主键ID',
	`f_customer_name`     	  VARCHAR(20) NOT NULL COMMENT '用户名',
	`f_customer_nick_name`	  VARCHAR(20) DEFAULT NULL COMMENT '用户昵称',
  `f_user_money`            DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '用户余额',
	`f_register_date`			    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT	'注册时间',
  `f_modified_time`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
	`f_is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
	PRIMARY KEY (`f_customer_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户表';

CREATE TABLE IF NOT EXISTS `t_address` (
	`f_customer_addr_id`      INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '地址表自增主键ID',
	`f_customer_id`			      INT UNSIGNED NOT NULL COMMENT 'customer表的自增ID',
  `f_zip`					          SMALLINT NOT NULL COMMENT '邮编',
	`f_province`			      	VARCHAR(20) NOT NULL COMMENT '省份',
	`f_city`					        VARCHAR(20) NOT NULL COMMENT '城市',
	`f_district`				      VARCHAR(20) NOT NULL COMMENT '区',
	`f_address`				        VARCHAR(200) NOT NULL COMMENT '具体地址',
	`f_is_default`			      TINYINT NOT NULL COMMENT '是否默认',
	`f_modified_time`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
	`f_create_date`		  	    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`f_is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
  PRIMARY KEY (`f_customer_addr_id`)
)ENGINE=innodb DEFAULT CHARSET=utf8 COMMENT '用户地址表';

CREATE TABLE IF NOT EXISTS `t_product` (
	`f_product_id`			      INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '商品ID',
  `f_product_core`          CHAR(16) NOT NULL COMMENT '商品编码',
	`f_product_name`			    VARCHAR(100) NOT NULL COMMENT '商品名称',
	`f_product_price`			    INT UNSIGNED NOT NULL COMMENT '商品单价',
	`f_product_descript`			TEXT DEFAULT NULL COMMENT '商品描述',
	`f_product_category`		  VARCHAR(10) DEFAULT 'default' COMMENT '商品分类',
  `f_publish_status`        TINYINT NOT NULL DEFAULT 0 COMMENT '上下架状态：0下架 1上架',
	`f_create_date`			      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '商品录入时间',
	`f_modified_date`		      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	`f_is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
	PRIMARY KEY (`f_product_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '商品信息表';

CREATE TABLE IF NOT EXISTS `t_order` (
	`f_order_id`				      INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '订单ID',
	`f_order_sn`				      BIGINT UNSIGNED NOT NULL COMMENT '订单编号 yyyymmddnnnnnnnn',
	`f_user_id`   			      INT UNSIGNED NOT NULL COMMENT '下单用户ID',
  `f_address`               INT UNSIGNED NOT NULL COMMENT '收货地址信息，用户地址表的自增主键ID',
	`f_payment_method`	      TINYINT NOT NULL COMMENT '支付方式：1现金，2余额，3网银，4支付宝，5微信',
	`f_order_money` 			    DECIMAL(8,2) NOT NULL COMMENT '订单金额',
  `f_district_money`		    DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
  `f_shipping_money`  		  DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '运费金额',
  `f_payment_money`	  		  DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '支付金额',
	`f_pay_date`			    	  TIMESTAMP DEFAULT NULL COMMENT '支付时间',
	`f_shipping_date`			    TIMESTAMP DEFAULT NULL COMMENT '发货时间',
	`f_receive_time`		    	TIMESTAMP DEFAULT NULL COMMENT '收货时间',
	`f_order_status`			    TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态',
	`f_create_date`			      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
	`f_update_date`			      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	`f_is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
	PRIMARY KEY (`f_order_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '订单主表';

CREATE TABLE IF NOT EXISTS `t_order_detail` (
	`f_order_detail_id` 	  	INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
	`f_order_id` 				      INT UNSIGNED NOT NULL COMMENT '订单ID',
	`f_product_id`			      INT UNSIGNED NOT NULL COMMENT '订单商品ID',
	`f_product_name`		    	VARCHAR(50) NOT NULL COMMENT '商品名称',
	`f_product_cnt`		      	INT NOT NULL DEFAULT 1 COMMENT '购买商品数量',
  `f_product_price`	    		DECIMAL(8,2) NOT NULL COMMENT '购买商品单价',
  `f_is_delete`				      TINYINT NOT NULL COMMENT '是否删除',
  `f_modified_time`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`f_order_detail_id`)
)ENGINE=innodb DEFAULT CHARSET=utf8 COMMENT '订单详情表';