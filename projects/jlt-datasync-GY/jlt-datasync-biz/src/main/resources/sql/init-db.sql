CREATE TABLE demo_order (
  `orderNo` varchar(50) NOT NULL,
  `customer` varchar(200) NOT NULL,
  `cost` float DEFAULT NULL,
  PRIMARY KEY (`orderNo`)
);

INSERT INTO demo_order VALUES ('20170601','demo',38),('20170602','demo',38),('20170606','demo',20);


CREATE TABLE `sys_code_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(100) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `quick_code` varchar(20) DEFAULT NULL,
  `sort_no` int(11) DEFAULT NULL,
  `color` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO sys_code_master(type, code, name, quick_code, sort_no, color)
VALUES('commonkey', 's1', 'gray', 's1', 1, 'gray'),
      ('key1', 's1', 'complete', 's1', 1, null),
      ('key2', 's1', 'complete', 's1', 1, null);



CREATE TABLE demo_warehouse (
	`id` long(20) NOT NULL,
	`name` varchar(100) NOT NULL,
	`build_time` datetime DEFAULT NULL,
	`contact_id` int(20) DEFAULT NULL,
    `contact` varchar(50) DEFAULT NULL,
    `tel` varchar(50) DEFAULT NULL,
    `area_code` varchar(50) DEFAULT NULL,
    `area_name` varchar(50) DEFAULT NULL,
    `city_code` varchar(50) DEFAULT NULL,
    `city_name` varchar(50) DEFAULT NULL,
    `province_code` varchar(50) DEFAULT NULL,
    `province_name` varchar(50) DEFAULT NULL,
	`address` varchar(200) DEFAULT NULL,
	`rec_status` int(1) DEFAULT NULL,
  	`rec_ver` int(8) DEFAULT NULL,
  	`creator` varchar(50) DEFAULT NULL,
  	`create_name` varchar(50) DEFAULT NULL,
  	`create_time` datetime NULL DEFAULT NULL,
  	`modifier` varchar(50) DEFAULT NULL,
  	`modify_name` varchar(50) DEFAULT NULL,
  	`modify_time` datetime NULL DEFAULT NULL,
  	PRIMARY KEY (`id`)
);

INSERT INTO demo_warehouse VALUES (2017110103,'上海仓3',SYSDATE(),2017110103,'路人甲3','13222222222',null,null,null,null,null,null,null,0,0,null,null,SYSDATE(),null,null,SYSDATE());
INSERT INTO demo_warehouse VALUES (2017110104,'上海仓4',SYSDATE(),2017110104,'路人甲4','13222222222',null,null,null,null,null,null,null,0,0,null,null,SYSDATE(),null,null,SYSDATE());
INSERT INTO demo_warehouse VALUES (2017110105,'上海仓5',SYSDATE(),2017110105,'路人甲5','13222222222',null,null,null,null,null,null,null,0,0,null,null,SYSDATE(),null,null,SYSDATE());
INSERT INTO demo_warehouse VALUES (2017110106,'上海仓6',SYSDATE(),2017110106,'路人甲6','13222222222',null,null,null,null,null,null,null,0,0,null,null,SYSDATE(),null,null,SYSDATE());
INSERT INTO demo_warehouse VALUES (2017110102,'北京仓',SYSDATE(),2017110102,'路人乙','13222222223',null,null,null,null,null,null,null,0,0,null,null,SYSDATE(),null,null,SYSDATE());
INSERT INTO demo_warehouse VALUES (2017110101,'上海仓',SYSDATE(),2017110101,'路人甲','13222222222',null,null,null,null,null,null,null,0,0,null,null,SYSDATE(),null,null,SYSDATE());

CREATE TABLE demo_device (
	`id` long(20) NOT NULL,
	`device_name` varchar(100) NOT NULL,
	`warehouse_id` long(20) NOT NULL,
  	PRIMARY KEY (`id`)
);

INSERT INTO demo_device VALUES (2017110101,'温度设备',2017110101);
INSERT INTO demo_device VALUES (2017110102,'湿度设备',2017110101);


/*==============================================================*/
/* Table: eb_favourite_goods                                    */
/*==============================================================*/
create table eb_favourite_goods
(
   id                   bigint(20) not null,
   goods_id             bigint(20) not null,
   tenant_id            bigint(20) not null,
   customer_id          bigint(20) not null,
   quality              int(10) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: eb_goods                                              */
/*==============================================================*/
create table eb_goods
(
   id                   bigint(20) not null,
   goods_name           varchar(100) not null,
   goods_code           varchar(64) not null,
   abbrev               varchar(100),
   description          varchar(500),
   category_id          bigint(20) not null,
   item_spec            varchar(50) not null,
   item_weight          decimal(18,2) not null,
   manufacturer         varchar(50),
   is_hot               tinyint(1) not null,
   is_commend           tinyint(1) not null,
   is_top               tinyint(1) not null,
   is_on_sale           tinyint(1) not null,
   tenant_id            bigint(20) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: eb_goods_category                                     */
/*==============================================================*/
create table eb_goods_category
(
   id                   bigint(20) not null,
   category_name        varchar(50) not null,
   category_no          bigint(64) not null,
   parent_id            bigint(20) not null,
   sort                int(10),
   tenant_id            bigint(20) not null,
   is_display           tinyint(1),
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: eb_goods_details                                      */
/*==============================================================*/
create table eb_goods_details
(
   goods_id             bigint(20) not null,
   detail_description   longtext,
   tenant_id            bigint(20) not null,
   spec_parameter       longtext,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (goods_id)
);

/*==============================================================*/
/* Table: eb_goods_price_history                                */
/*==============================================================*/
create table eb_goods_price_history
(
   id                   bigint(20) not null,
   goods_id             bigint(20) not null,
   tenant_id            bigint(20) not null,
   min_quality_unit     varchar(20) not null,
   purchase_price       decimal(18,2) not null,
   sale_price           decimal(18,2) not null,
   max_sale_price       decimal(18,2) not null,
   min_sale_price       decimal(18,2) not null,
   status               varchar(10) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: eb_goods_promotion_history                            */
/*==============================================================*/
create table eb_goods_promotion_history
(
   id                   bigint(20) not null,
   goods_id             bigint(20) not null,
   tenant_id            bigint(20) not null,
   promotion_price      decimal(18,2) not null,
   max_quality          int(10) not null,
   start_time           datetime,
   end_time             datetime,
   status               varchar(10) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: eb_shopping_cart                                      */
/*==============================================================*/
create table eb_shopping_cart
(
   id                   bigint(20) not null,
   goods_id             bigint(20) not null,
   tenant_id            bigint(20) not null,
   customer_id          bigint(20) not null,
   quality              int(10) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: ef_order_cost                                         */
/*==============================================================*/
create table ef_order_cost
(
   id                   bigint(20) not null auto_increment comment '主键',
   cost_code            varchar(20) not null default '' comment '费用编码',
   cost_name            varchar(50) not null default '' comment '费用名称',
   order_id             bigint(20) not null default 0 comment '订单主键',
   order_no             varchar(50) not null default '' comment '订单号',
   cost_amount          decimal(10,2) not null default 0 comment '费用金额',
   customer_id          bigint(20) not null default 0 comment '客户主键',
   tenant_id            bigint(20) not null default 0 comment '租户主键',
   cost_type            tinyint not null default 0 comment '费用类型',
   currency_code        varchar(10) not null default '' comment '币种',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table ef_order_cost comment '订单费用表';

/*==============================================================*/
/* Table: ef_receivables_history                                */
/*==============================================================*/
create table ef_receivables_history
(
   id                   bigint(20) not null comment '主键',
   order_id             bigint(20) not null default 0 comment '订单主键',
   order_code           varchar(50) not null default '' comment '订单号',
   receivables_time     datetime comment '付款时间',
   receivables_amount   decimal(10,2) default 0,
   receivables_way      varchar(50) not null default '',
   remark               varchar(800) not null default '',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table ef_receivables_history comment '收款历史表';

/*==============================================================*/
/* Table: eo_delivery_batch                                     */
/*==============================================================*/
create table eo_delivery_batch
(
   id                   bigint(20) not null auto_increment comment '主键',
   delivery_no          varchar(50) not null default '' comment '发货单号',
   elo_id               bigint(20) default 0 comment '物流单主键',
   elo_no               varchar(50) not null default '' comment '物流单号',
   carrier_id           varchar(50) not null default '' comment '承运商',
   carrier_code         varchar(50) not null default '' comment '承运商编码',
   carrier_name         varchar(50) not null default '' comment '承运商名称',
   tenant_id            bigint(20) not null default 0 comment '租户主键',
   delivery_wh_id       varchar(50) not null default '' comment '出库仓库主键',
   delivery_wh_code     varchar(50) not null default '' comment '出库仓库编码',
   delivery_wh_name     varchar(50) not null default '' comment '出库仓库名称',
   delivery_wh_province_code varchar(50) not null default '' comment '出库仓库省份',
   delivery_wh_province_name varchar(50) not null default '' comment '出库仓库省份名称',
   delivery_wh_city_code varchar(50) not null default '' comment '出库仓库城市',
   delivery_wh_city_name varchar(50) not null default '' comment '出库仓库城市名称',
   delivery_wh_area_code varchar(50) not null default '' comment '出库仓库区域',
   delivery_wh_area_name varchar(50) not null default '' comment '出库仓库区域名称',
   delivery_wh_address  varchar(50) not null default '' comment '出库仓库地址',
   delivery_wh_contacts varchar(50) not null default '' comment '出库仓库联系人',
   delivery_wh_telephone varchar(50) not null default '' comment '出库仓库联系电话',
   delivery_wh_Fixed_telephone varchar(50) not null default '' comment '出库仓库固定电话',
   order_status         varchar(50) not null default '' comment '发货单状态',
   delivery_time        datetime default NULL comment '要求送达时间',
   delivery_total_amount decimal(10,2) not null default 0 comment '发货金额',
   remark               varchar(800) not null default '' comment '备注',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_delivery_batch comment '发货单表';

/*==============================================================*/
/* Table: eo_delivery_batch_goods                               */
/*==============================================================*/
create table eo_delivery_batch_goods
(
   id                   bigint(20) not null auto_increment comment '主键',
   delivery_order_id    bigint(20) not null default 0 comment '发货单主键',
   delivery_order_no    varchar(50) not null default '' comment '发货单编码',
   order_goods_id       bigint(20) not null default 0 comment '订单商品主键',
   goods_id             bigint(20) not null default 0 comment '商品主键',
   goods_no             varchar(64) not null default '' comment '商品编码',
   goods_name           varchar(50) not null default '' comment '商品名称',
   goods_price_history_id bigint(20) not null default 0 comment '商品价格历史主键',
   customer_id          bigint(20) not null default 0 comment '客户主键',
   delivery_num         decimal(10,3) not null default 0 comment '发货数量',
   remark               varchar(800) not null default '' comment '备注',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_delivery_batch_goods comment '发货商品表';

/*==============================================================*/
/* Table: eo_order                                              */
/*==============================================================*/
create table eo_order
(
   id                   bigint(20) not null auto_increment comment '主键',
   order_no             varchar(50) not null default '' comment '订单编号',
   place_order_time     datetime default NULL comment '下单时间',
   order_total_amount   decimal(10,2) not null default 0 comment '订单总金额',
   customer_id          bigint(20) not null default 0 comment '客户主键',
   tenant_id            bigint(20) not null default 0 comment '租户主键',
   elo_status           tinyint not null default 0,
   consignee_name       varchar(50) not null default '' comment '收件人',
   province_code        varchar(50) not null default '' comment '省份编码',
   province_name        varchar(50) not null default '' comment '省份名称',
   city_code            varchar(50) not null default '' comment '市编码',
   city_name            varchar(50) not null default '' comment '市名称',
   area_code            varchar(50) not null default '' comment '区域编码',
   area_name            varchar(50) not null default '' comment '区域名称',
   address              varchar(200) not null default '' comment '详细地址',
   phone_number         varchar(20) not null default '' comment '手机号码',
   fixed_telephone      varchar(20) not null default '' comment '固定电话',
   pay_way              varchar(20) not null default '0' comment '支付方式(数据字典定义)',
   payment_amount       decimal(10,3) not null default 0,
   paid_amount          decimal(10,3) not null default 0,
   unpaid_amount        decimal(10,3) not null default 0,
   invoice_type         varchar(20) not null default '' comment '发票类型(数据字典定义)',
   invoice_title        varchar(100) not null default '' comment '发票抬头',
   delivery_time        datetime default NULL comment '交货时间',
   remark               varchar(800) not null default '' comment '订单备注',
   order_status         tinyint not null default 0 comment '订单状态(0:已创建,10::已审核,20:拣货中,30:配送中,40:已签收,50:已评论,60:已取消)',
   place_order_way      tinyint not null default 0 comment '下单方式(10:微信下单,20:代客下单)',
   pay_status           varchar(20) not null default '' comment '付款方式',
   discount_total_amount decimal(10,2) not null default 0 comment '优惠总额',
   transport_total_amount decimal(10,2) not null default 0 comment '运费总额',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_order comment '订单表';

/*==============================================================*/
/* Table: eo_order_goods                                        */
/*==============================================================*/
create table eo_order_goods
(
   id                   bigint(20) not null auto_increment comment '主键',
   order_no             varchar(50) not null default '' comment '订单编号',
   order_id             bigint(20) not null,
   goods_id             bigint(20) not null default 0 comment '商品表主键',
   goods_price_history_id bigint(20) not null default 0 comment '商品价格历史主键',
   goods_num            decimal(10,3) not null default 0 comment '商品数量',
   goods_no             varchar(64) not null default '' comment '商品编码',
   original_price       decimal(10,2) not null default 0 comment '商品原价',
   discount_price       decimal(10,2) not null default 0 comment '折后价格',
   discount_amount      decimal(10,2) not null default 0 comment '优惠金额',
   discount_total_amount decimal(10,2) not null default 0,
   goods_total_amount   decimal(10,2) not null default 0 comment '商品总金额',
   goods_total_weight   decimal(10,3) not null default 0,
   delivered_num        decimal(10,3) not null default 0 comment '已发货量',
   received_num         decimal(10,3) not null default 0 comment '收货数量',
   sales_return_num     decimal(10,3) not null default 0 comment '退货数量',
   remark               varchar(800) not null default '' comment '商品备注',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_order_goods comment '订单商品表';

/*==============================================================*/
/* Table: eo_order_route                                        */
/*==============================================================*/
create table eo_order_route
(
   id                   bigint(20) not null auto_increment comment '主键',
   order_id             bigint(20) not null default 0 comment '订单主键',
   order_no             varchar(50) not null default '' comment '订单号',
   status_code          varchar(20) not null default '' comment '状态码',
   status_name          varchar(50) not null default '' comment '状态名称',
   route_time           datetime default NULL comment '路由时间',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_order_route comment '订单路由表';

/*==============================================================*/
/* Table: eo_return_order_route                                 */
/*==============================================================*/
create table eo_return_order_route
(
   id                   bigint(20) not null auto_increment comment '主键',
   return_order_id      bigint(20) not null default 0 comment '订单主键',
   return_order_no      varchar(50) not null default '' comment '订单号',
   status_code          varchar(20) not null default '' comment '状态码',
   status_name          varchar(50) not null default '' comment '状态名称',
   route_time           datetime default NULL comment '路由时间',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_return_order_route comment '退货路由表';

/*==============================================================*/
/* Table: eo_sales_return_order                                 */
/*==============================================================*/
create table eo_sales_return_order
(
   id                   bigint(20) not null auto_increment comment '主键',
   sales_return_order_no varchar(50) not null default '' comment '退货单号',
   order_id             bigint(20) not null default 0 comment '退货订单主键',
   order_no             varchar(50) not null default '' comment '退货订单编码',
   customer_id          bigint(20) not null default 0 comment '退货客户',
   tenant_id            bigint(20) not null default 0 comment '租户主键',
   sales_return_time    datetime default NULL,
   order_status         tinyint not null default 0 comment '退货状态',
   sales_return_reason  varchar(100) not null default '' comment '退货原因',
   remark               varchar(800) not null default '' comment '备注',
   sales_return_total_amount decimal(10,2) not null default 0 comment '退货总额',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_sales_return_order comment '退货单';

/*==============================================================*/
/* Table: eo_sales_return_order_goods                           */
/*==============================================================*/
create table eo_sales_return_order_goods
(
   id                   bigint(20) not null auto_increment comment '主键',
   sales_return_order_id bigint(20) not null default 0 comment '退货单主键',
   sales_return_order_code varchar(50) not null default '' comment '退货单编码',
   order_goods_id       bigint(20) not null default 0 comment '订单商品主键',
   goods_id             bigint(20) not null default 0 comment '商品主键',
   goods_no             varchar(64) not null default '' comment '商品编码',
   goods_name           varchar(50) not null default '' comment '商品名称',
   goods_price_history_id bigint(20) not null default 0 comment '商品价格历史主键',
   customer_id          bigint(20) not null default 0 comment '客户主键',
   sales_return_num     decimal(10,3) not null default 0 comment '退货数量',
   sales_return_goods_amount decimal(10,2) not null default 0 comment '商品退货总额',
   remark               varchar(800) not null default '' comment '备注',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table eo_sales_return_order_goods comment '退货商品表';

/*==============================================================*/
/* Table: es_attachment                                         */
/*==============================================================*/
create table es_attachment
(
   id                   bigint(20) not null auto_increment comment '主键',
   business_code        varchar(20) not null default '' comment '业务类型',
   business_id          varchar(20) not null default '' comment '业务主键',
   file_name            varchar(200) not null default '' comment '上传文件名',
   save_name            varchar(200) not null default '' comment '保存文件名',
   file_type            varchar(100) not null default '' comment '文件类型',
   file_url             varchar(200) not null default '' comment '文件地址',
   file_size            decimal(10,2) not null default 0 comment '文件大小',
   download_code        varchar(100) not null default '' comment '下载编号',
   customer_id          bigint(20) not null default 0 comment '客户主键',
   tenant_id            bigint(20) not null default 0 comment '租户主键',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table es_attachment comment '附件表';

/*==============================================================*/
/* Table: es_config                                             */
/*==============================================================*/
create table es_config
(
   id                   bigint(20) not null auto_increment comment '主键',
   config_type          varchar(20) not null default '' comment '配置类型',
   customer_id          bigint(20) not null default 0 comment '客户主键',
   tenant_id            bigint(20) not null default 0 comment '租户主键',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table es_config comment '系统配置表';

/*==============================================================*/
/* Table: es_config_detail                                      */
/*==============================================================*/
create table es_config_detail
(
   id                   bigint(20) not null auto_increment comment '主键',
   config_id            bigint(20) not null default 0 comment '配置表主键',
   config_item_type     varchar(50) not null default '' comment '明细类型',
   config_item_content  varchar(400) not null default '' comment '配置明细内容',
   config_item_sketch   longtext comment '图文内容',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table es_config_detail comment '系统配置明细表';

/*==============================================================*/
/* Table: es_customer                                           */
/*==============================================================*/
create table es_customer
(
   id                   bigint(20) not null,
   tenant_id            bigint(20) not null,
   customer_code        varchar(50) not null,
   customer_name        varchar(50),
   linkman              varchar(50),
   mobilephone          varchar(20),
   province_id          varchar(100),
   province_name        varchar(100),
   city_id              varchar(100),
   city_name            varchar(100),
   area_id              varchar(100),
   area_name            varchar(100),
   login_name           varchar(50),
   password             varchar(50),
   address              varchar(500),
   status               varchar(10) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: es_customer_address                                   */
/*==============================================================*/
create table es_customer_address
(
   id                   bigint(20) not null,
   item_id              bigint(20) not null,
   tenant_id            bigint(20) not null,
   customer_id          bigint(20) not null,
   linkman              varchar(100),
   mobilephone          varchar(20),
   province_id          varchar(100),
   province_name        varchar(100),
   city_id              varchar(100),
   city_name            varchar(100),
   area_id              varchar(100),
   area_name            varchar(100),
   address              varchar(500),
   is_default           tinyint(1),
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: es_dictionary                                         */
/*==============================================================*/
create table es_dictionary
(
   id                   bigint(20) not null,
   type                 varchar(50) not null,
   code                 varchar(50) not null,
   name                 varchar(50) not null,
   sort                 varchar(10),
   remark               varchar(800),
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: es_system_notify                                      */
/*==============================================================*/
create table es_system_notify
(
   id                   bigint(20) not null,
   tenant_id            bigint(20) not null,
   type                 varchar(100) not null,
   title                varchar(200) not null,
   content              longtext,
   status               varchar(10) not null,
   public_time          datetime not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: es_tenant                                             */
/*==============================================================*/
create table es_tenant
(
   id                   bigint(20) not null,
   name                 varchar(50) not null,
   code                 varchar(50) not null,
   biz_license          varchar(50),
   create_year          varchar(20),
   abbrev               varchar(50),
   long_name            varchar(50),
   address              varchar(50),
   email                varchar(50),
   fax                  varchar(50),
   legal_person         varchar(50),
   tele_phone           varchar(20),
   mobile_phone         varchar(20),
   py_code              varchar(100),
   taxregister_no       varchar(50),
   summary              varchar(800),
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: es_tenant_profile                                     */
/*==============================================================*/
create table es_tenant_profile
(
   tenant_id            bigint(20) not null,
   db_setting           varchar(800),
   primary key (tenant_id)
);

/*==============================================================*/
/* Table: es_tenant_user                                        */
/*==============================================================*/
create table es_tenant_user
(
   id                   bigint(20) not null,
   tenant_id            bigint(20) not null,
   tenant_name          varchar(50),
   user_no              varchar(50),
   email                varchar(100),
   mobilephone          varchar(20),
   esus_sex             varchar(1),
   telephone            varchar(50),
   user_code            varchar(50) not null,
   user_name            varchar(50),
   userpwd              varchar(50) not null,
   user_type            varchar(5) not null,
   idcarno              varchar(20),
   pswstrength          int(10),
   regipaddress         varchar(50),
   user_source          varchar(10),
   status               varchar(10) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: mid_order_delivery                                    */
/*==============================================================*/
create table mid_order_delivery
(
   id                   bigint(20) not null auto_increment comment '主键',
   order_id             bigint(20) not null default 0 comment '订单主键',
   order_no             varchar(50) not null default '' comment '订单号',
   delivery_order_id    bigint(20) not null default 0 comment '发货单主键',
   delivery_order_no    varchar(50) not null default '' comment '发货单号',
   elo_id               bigint(20) not null default 0 comment '物流单主键',
   elo_no               varchar(50) not null default '' comment '物流单号',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

--alter table mid_order_delivery comment '订单发货中间表';

/*==============================================================*/
/* Table: mid_order_goods_promotion                             */
/*==============================================================*/
create table mid_order_goods_promotion
(
   id                   bigint(20) not null auto_increment comment '主键',
   order_item_id        bigint(20) not null default 0 comment '订单商品主键',
   order_id             bigint(20) not null default 0 comment '订单主键',
   order_no             varchar(20) not null default '' comment '订单号',
   item_id              bigint(20) not null default 0 comment '商品主键',
   rule_id              bigint(20) not null default 0 comment '规则表主键',
   rule_type            varchar(20) not null default '' comment '规则类型',
   item_price_history_id bigint(20) not null default 0 comment '商品价格历史主键',
   rec_status           tinyint(1) not null default 0 comment '删除标识(0:未删除,1:已删除)',
   rec_ver              int(4) not null default 0 comment '版本号',
   creator              varchar(50) not null default '' comment '创建人',
   create_name          varchar(50) not null default '' comment '创建人名称',
   create_time          datetime  comment '创建时间',
   modifier             varchar(50) not null default '' comment '修改人',
   modify_name          varchar(50) not null default '' comment '修改人名称',
   modify_time          datetime   comment '修改时间',
   primary key (id)
);

create table es_tenant_introduce
(
   id                   bigint(20) not null,
   tenant_id            bigint(20) not null,
   name                 varchar(50) not null,
   introduce            text,
   contact_no           varchar(50),
   sort                 int(10) not null,
   status               varchar(10) not null,
   rec_status           int(10) not null,
   rec_ver              int(8),
   creator              varchar(50),
   create_name          varchar(50),
   create_time          datetime,
   modifier             varchar(50),
   modify_name          varchar(50),
   modify_time          datetime,
   primary key (id)
);

INSERT INTO "PUBLIC"."EO_ORDER" (
	"ID",
	"ORDER_NO",	"PLACE_ORDER_TIME",	"ORDER_TOTAL_AMOUNT",	"CUSTOMER_ID",
	"TENANT_ID",	"ELO_STATUS",	"CONSIGNEE_NAME",
	"PROVINCE_CODE",	"PROVINCE_NAME",	"CITY_CODE",
	"CITY_NAME",	"AREA_CODE",	"AREA_NAME",	"ADDRESS",
	"PHONE_NUMBER",	"FIXED_TELEPHONE",	"PAY_WAY",	"PAYMENT_AMOUNT",
	"PAID_AMOUNT",	"UNPAID_AMOUNT",	"INVOICE_TYPE",	"INVOICE_TITLE",
	"DELIVERY_TIME",	"REMARK",	"ORDER_STATUS",	"PLACE_ORDER_WAY",
	"PAY_STATUS",	"DISCOUNT_TOTAL_AMOUNT",	"TRANSPORT_TOTAL_AMOUNT",	"REC_STATUS",
	"REC_VER",	"CREATOR",	"CREATE_NAME",	"CREATE_TIME",	"MODIFIER",	"MODIFY_NAME",	"MODIFY_TIME"
)
VALUES
	(
		'111',		'11',
		'2017-11-07 00:00:00',		'11',		'11',
		'11',		'11',		'11',		'11',		'11',		'11',
		'11',		'11',		'11',		'11',
		'11',		'11',		'11',		'11',		'11',		'11',		'11',
		'11',		'2017-11-07 00:00:00',		'11',
		'11',		'11',		'11',		'11',		'11',		'11',		'11',
		'11',		'11',		'2017-11-07 00:00:00',		'11',		'11',		'2017-11-07 00:00:00'
	);


INSERT INTO `es_customer` VALUES ('1', '001', 'DJXISAWD', '小小', '于晓', '110110110', '001', '福建省', '001', '厦门', '001', '湖里区', '123', '123', '1', 'Y', '0', '1', 'max', 'max', '2017-11-06 18:16:55', 'max', 'max', '2017-11-06 18:17:05');
INSERT INTO `es_customer` VALUES ('2', '002', 'DJXISAWD', '天天', '于晓', '110110110', '001', '福建省', '001', '厦门', '001', '湖里区', '123', '123', '1', 'Y', '0', '1', 'max', 'max', '2017-11-06 18:16:55', 'max', 'max', '2017-11-06 18:17:05');

INSERT INTO `eo_sales_return_order`  VALUES ('1', '01', '1', 'OA1', '1', '1', '2017-11-06 18:16:55', '1', '不想要', '无', '100.00', '1', '1', 'max', 'max', '2017-11-06 18:21:09', 'max', 'max', '2017-11-06 18:16:55');
INSERT INTO `eo_sales_return_order`  VALUES ('2', '02', '2', 'OA1', '2', '0', '2017-11-06 18:16:55', '0', '不想要', '无', '100.00', '0', '1', 'max', 'max', '2017-11-06 18:21:09', 'max', 'max', '2017-11-06 18:16:55');
INSERT INTO `eo_sales_return_order`  VALUES ('3', '01', '1', 'OA1', '1', '1', '2017-11-06 18:16:55', '1', '不想要', '无', '100.00', '1', '1', 'max', 'max', '2017-11-06 18:21:09', 'max', 'max', '2017-11-06 18:16:55');
INSERT INTO `eo_sales_return_order`  VALUES ('4', '02', '2', 'OA1', '2', '0', '2017-11-06 18:16:55', '0', '不想要', '无', '100.00', '0', '1', 'max', 'max', '2017-11-06 18:21:09', 'max', 'max', '2017-11-06 18:16:55');


INSERT INTO es_system_notify ( id, TYPE, title,content,status,public_time,create_name,create_time,tenant_id,REC_STATUS,REC_VER ) VALUES ( 928091557284900865, '系统通知','aaaa','bbbbb', '1', '1900-11-12 11:12:13', '张三丰', SYSDATE(), 1, 0, 0 ); 