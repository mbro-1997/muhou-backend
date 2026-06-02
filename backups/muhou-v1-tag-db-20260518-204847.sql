-- MySQL dump 10.13  Distrib 5.7.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: muhou
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `muhou`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `muhou` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `muhou`;

--
-- Table structure for table `factory_invite_code`
--

DROP TABLE IF EXISTS `factory_invite_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `factory_invite_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '邀请码主键ID',
  `code_hash` varchar(128) NOT NULL COMMENT '邀请码哈希值',
  `code_plain` varchar(64) DEFAULT NULL COMMENT '邀请码明文，演示阶段用于后续查看，生产环境建议加密存储',
  `code_suffix` varchar(16) NOT NULL COMMENT '邀请码后缀',
  `status` varchar(20) NOT NULL DEFAULT 'unused' COMMENT '状态：unused/locked/used/expired/revoked',
  `expire_at` datetime NOT NULL COMMENT '过期时间',
  `created_by_admin_user_id` bigint(20) NOT NULL COMMENT '创建管理员用户ID',
  `locked_by_user_id` bigint(20) DEFAULT NULL COMMENT '锁定用户ID',
  `locked_at` datetime DEFAULT NULL COMMENT '锁定时间',
  `used_by_user_id` bigint(20) DEFAULT NULL COMMENT '使用用户ID',
  `used_at` datetime DEFAULT NULL COMMENT '使用时间',
  `revoked_by_admin_user_id` bigint(20) DEFAULT NULL COMMENT '作废管理员用户ID',
  `revoked_at` datetime DEFAULT NULL COMMENT '作废时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '邀请码备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factory_invite_code_hash` (`code_hash`),
  KEY `idx_factory_invite_code_status` (`status`),
  KEY `idx_factory_invite_code_suffix` (`code_suffix`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='工厂入驻邀请码表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factory_invite_code`
--

LOCK TABLES `factory_invite_code` WRITE;
/*!40000 ALTER TABLE `factory_invite_code` DISABLE KEYS */;
INSERT INTO `factory_invite_code` VALUES (1,'d1f4db492294ecc7e9bca009cf9ffd96024537d8632234627bd9aff1909ac773',NULL,'6Q5KS4','revoked','2026-05-04 16:15:53',3,NULL,NULL,NULL,NULL,3,'2026-04-27 17:40:39',NULL,'2026-04-27 16:15:53','2026-04-27 17:40:39'),(2,'bba51a04d56f8151e7527ac6faf3591d07e8d00c6e8bc0b149e9bb5d7f5ed344',NULL,'JRVFCN','revoked','2026-05-04 17:40:25',3,NULL,NULL,NULL,NULL,3,'2026-04-27 17:40:37',NULL,'2026-04-27 17:40:24','2026-04-27 17:40:37'),(3,'fd3180732ef7b958d6c6fc8b379a5dd3d202a28b57e718045f5a13878edd7aba',NULL,'CRRSYV','revoked','2026-05-04 17:40:55',3,NULL,NULL,NULL,NULL,3,'2026-04-27 17:41:41',NULL,'2026-04-27 17:40:54','2026-04-27 17:41:41'),(4,'0326a5078e74a01e1f864fdc6cd90b0352bd127953167b28b2610865037f8890',NULL,'DCPHU8','used','2026-05-04 17:41:42',3,6,'2026-04-27 17:42:29',6,'2026-04-27 17:45:41',NULL,NULL,NULL,'2026-04-27 17:41:42','2026-04-27 17:45:41'),(5,'712a5d3f63b6b08d62493baa9223068005a227a80a663ec24609eef1706ebba9','MUHOU-AE5U-7L3Q44','7L3Q44','locked','2026-05-05 09:12:33',3,6,'2026-04-28 09:15:25',NULL,NULL,NULL,NULL,NULL,'2026-04-28 09:12:32','2026-04-28 09:15:25'),(6,'4d1410e5be7e5160f46b416dc53711f873c85c93b5504721c3b8506a188c4876','MUHOU-LGCM-ATMM2G','ATMM2G','revoked','2026-05-05 09:13:31',3,NULL,NULL,NULL,NULL,3,'2026-04-28 09:14:22',NULL,'2026-04-28 09:13:31','2026-04-28 09:14:22'),(7,'c637452d0974983c885b171358d21cd50e29e783b63b57a845683b5242bfea97','MUHOU-9DWG-DM4EQN','DM4EQN','locked','2026-05-05 17:07:55',3,6,'2026-04-28 17:08:38',NULL,NULL,NULL,NULL,NULL,'2026-04-28 17:07:55','2026-04-28 17:08:38'),(8,'6870068b0cec2a0c939ad491b9f80cb3badc26a3dd7619b27dbeeaf33b082df6','MUHOU-UZ3D-8AV48G','8AV48G','used','2026-05-06 11:45:54',3,6,'2026-04-29 11:46:51',6,'2026-04-29 14:41:25',NULL,NULL,NULL,'2026-04-29 11:45:53','2026-04-29 14:41:25'),(9,'492b5cd3ad5477789e63587993806ae2c4167e7b84469020555dc049b7a2aed4','MUHOU-6AFT-J64DKV','J64DKV','used','2026-05-06 14:42:03',3,6,'2026-04-29 14:42:13',6,'2026-04-29 14:42:56',NULL,NULL,NULL,'2026-04-29 14:42:02','2026-04-29 14:42:56'),(10,'08d144f1eb886f8c08bf1aec3231334847e47c2cebee8d7505bb0c90c1909027','MUHOU-2QDZ-YF32UW','YF32UW','used','2026-05-06 14:44:38',3,6,'2026-04-29 14:44:53',6,'2026-04-29 14:45:22',NULL,NULL,NULL,'2026-04-29 14:44:38','2026-04-29 14:45:22'),(11,'d91f0e18eda1d5458873e02fca7c9c566520cc23664fce5d028a1c0c08fb4f54','MUHOU-4VU6-ZR5MUW','ZR5MUW','used','2026-05-06 20:09:11',3,6,'2026-04-29 20:09:57',6,'2026-04-29 20:11:59',NULL,NULL,NULL,'2026-04-29 20:09:10','2026-04-29 20:11:59');
/*!40000 ALTER TABLE `factory_invite_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `factory_profile`
--

DROP TABLE IF EXISTS `factory_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `factory_profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工厂主体主键ID',
  `owner_user_id` bigint(20) NOT NULL COMMENT '工厂所属用户ID',
  `factory_name` varchar(128) NOT NULL COMMENT '工厂名称',
  `unified_social_credit_code` varchar(32) NOT NULL COMMENT '统一社会信用代码',
  `business_license_url` varchar(255) NOT NULL COMMENT '营业执照图片地址',
  `contact_name` varchar(64) NOT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) NOT NULL COMMENT '联系人手机号',
  `factory_address` varchar(255) NOT NULL COMMENT '工厂地址',
  `main_business` varchar(255) DEFAULT NULL COMMENT '主营业务',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '工厂状态：active/disabled',
  `approved_audit_id` bigint(20) DEFAULT NULL COMMENT '审核通过的入驻审核ID',
  `approved_by_admin_user_id` bigint(20) DEFAULT NULL COMMENT '审核通过管理员ID',
  `approved_at` datetime DEFAULT NULL COMMENT '审核通过时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factory_profile_owner` (`owner_user_id`),
  UNIQUE KEY `uk_factory_profile_credit_code` (`unified_social_credit_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='正式工厂主体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factory_profile`
--

LOCK TABLES `factory_profile` WRITE;
/*!40000 ALTER TABLE `factory_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `factory_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','baseline schema','SQL','V1__baseline_schema.sql',2111479832,'root','2026-05-12 09:40:49',183,1),(2,'2','add order fund flow','SQL','V2__add_order_fund_flow.sql',-1604060967,'root','2026-05-12 09:42:14',87,1),(3,'3','add payment order link','SQL','V3__add_payment_order_link.sql',2029066420,'root','2026-05-12 09:42:14',219,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_dispute`
--

DROP TABLE IF EXISTS `order_dispute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_dispute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '纠纷主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '关联订单ID',
  `apply_user_id` bigint(20) NOT NULL COMMENT '发起纠纷用户ID',
  `applicant_role` varchar(20) NOT NULL DEFAULT 'demander',
  `apply_stage` varchar(32) NOT NULL DEFAULT 'unknown',
  `title` varchar(128) NOT NULL COMMENT '纠纷标题',
  `content` varchar(2000) NOT NULL COMMENT '纠纷描述',
  `claim_amount_fen` int(11) NOT NULL DEFAULT '0',
  `deposit_amount_fen_snapshot` int(11) NOT NULL DEFAULT '0',
  `order_status_snapshot` varchar(32) DEFAULT NULL,
  `demander_user_id` bigint(20) DEFAULT NULL,
  `supplier_user_id` bigint(20) DEFAULT NULL,
  `evidence_urls` varchar(2000) DEFAULT NULL,
  `dispute_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '纠纷状态：pending待处理 resolved已裁定 closed已关闭',
  `resolution` varchar(2000) DEFAULT NULL COMMENT '裁定结果',
  `resolution_type` varchar(20) DEFAULT NULL,
  `refund_status` varchar(32) NOT NULL DEFAULT 'none',
  `reviewer_id` bigint(20) DEFAULT NULL COMMENT '裁定管理员ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `resolved_at` datetime DEFAULT NULL COMMENT '裁定时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_dispute_order_status` (`order_id`,`dispute_status`),
  KEY `idx_order_dispute_applicant` (`apply_user_id`,`applicant_role`,`dispute_status`),
  KEY `idx_order_dispute_participant_status` (`demander_user_id`,`supplier_user_id`,`dispute_status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='订单纠纷表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_dispute`
--

LOCK TABLES `order_dispute` WRITE;
/*!40000 ALTER TABLE `order_dispute` DISABLE KEYS */;
INSERT INTO `order_dispute` VALUES (1,15,3,'supplier','wait_review','订单仲裁申请-ORD-1778149154636','要求赔付',1000,50000,'wait_review',1,3,'wxfile://tmp_e1796e2d912239d9d30b75511d1752d5.jpg','resolved','管理员裁定：双方各承担 50% 责任（演示）',NULL,'none',3,'2026-05-08 18:17:42','2026-05-08 18:19:31');
/*!40000 ALTER TABLE `order_dispute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_fund_flow`
--

DROP TABLE IF EXISTS `order_fund_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_fund_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flow_no` varchar(64) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `dispute_id` bigint(20) DEFAULT NULL,
  `payment_id` bigint(20) DEFAULT NULL,
  `flow_type` varchar(40) NOT NULL,
  `flow_direction` varchar(40) NOT NULL,
  `payer_role` varchar(32) NOT NULL DEFAULT 'platform',
  `payer_user_id` bigint(20) DEFAULT NULL,
  `receiver_role` varchar(32) NOT NULL,
  `receiver_user_id` bigint(20) DEFAULT NULL,
  `amount_fen` int(11) NOT NULL,
  `currency` varchar(8) NOT NULL DEFAULT 'CNY',
  `channel_action` varchar(40) NOT NULL,
  `channel_status` varchar(32) NOT NULL DEFAULT 'created',
  `wechat_out_no` varchar(64) DEFAULT NULL,
  `wechat_transaction_id` varchar(64) DEFAULT NULL,
  `wechat_response` json DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_fund_flow_no` (`flow_no`),
  KEY `idx_order_fund_flow_order` (`order_id`,`flow_type`),
  KEY `idx_order_fund_flow_dispute` (`dispute_id`),
  KEY `idx_order_fund_flow_receiver` (`receiver_role`,`receiver_user_id`),
  KEY `idx_order_fund_flow_status` (`channel_status`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_fund_flow`
--

LOCK TABLES `order_fund_flow` WRITE;
/*!40000 ALTER TABLE `order_fund_flow` DISABLE KEYS */;
INSERT INTO `order_fund_flow` VALUES (1,'RF-1778578940630-7b900d9cd1',15,NULL,15,'deposit_refund','platform_to_user','platform',NULL,'user',1,50000,'CNY','refund','success','RF-1778578940630-7b900d9cd1','mock-refund-RF-1778578940630-7b900d9cd1','{\"mock\": true, \"action\": \"refund\", \"amountFen\": 50000, \"outRefundNo\": \"RF-1778578940630-7b900d9cd1\"}','正常订单结束，退还押金','2026-05-12 17:42:20','2026-05-12 17:42:20'),(2,'PS-1778578940674-64cf2ccb51',15,NULL,15,'platform_commission','platform_to_receiver','platform',NULL,'platform_admin',NULL,6400,'CNY','profit_sharing','success','PS-1778578940674-64cf2ccb51','mock-profit-sharing-PS-1778578940674-64cf2ccb51','{\"mock\": true, \"action\": \"profit_sharing\", \"amountFen\": 6400, \"outOrderNo\": \"PS-1778578940674-64cf2ccb51\", \"receiverRole\": \"platform_admin\"}','正常订单结束，平台收取实际费用20%','2026-05-12 17:42:20','2026-05-12 17:42:20'),(3,'PS-1778578940686-0140a8d2b0',15,NULL,15,'supplier_settlement','platform_to_receiver','platform',NULL,'supplier',3,25600,'CNY','profit_sharing','success','PS-1778578940686-0140a8d2b0','mock-profit-sharing-PS-1778578940686-0140a8d2b0','{\"mock\": true, \"action\": \"profit_sharing\", \"amountFen\": 25600, \"outOrderNo\": \"PS-1778578940686-0140a8d2b0\", \"receiverRole\": \"supplier\"}','正常订单结束，工厂获得实际费用80%','2026-05-12 17:42:20','2026-05-12 17:42:20'),(4,'RF-1778578940785-87fbd93698',16,NULL,16,'deposit_refund','platform_to_user','platform',NULL,'user',1,50000,'CNY','refund','success','RF-1778578940785-87fbd93698','mock-refund-RF-1778578940785-87fbd93698','{\"mock\": true, \"action\": \"refund\", \"amountFen\": 50000, \"outRefundNo\": \"RF-1778578940785-87fbd93698\"}','正常订单结束，退还押金','2026-05-12 17:42:20','2026-05-12 17:42:20'),(5,'PS-1778578940796-9d2e3d89b0',16,NULL,16,'platform_commission','platform_to_receiver','platform',NULL,'platform_admin',NULL,9600,'CNY','profit_sharing','success','PS-1778578940796-9d2e3d89b0','mock-profit-sharing-PS-1778578940796-9d2e3d89b0','{\"mock\": true, \"action\": \"profit_sharing\", \"amountFen\": 9600, \"outOrderNo\": \"PS-1778578940796-9d2e3d89b0\", \"receiverRole\": \"platform_admin\"}','正常订单结束，平台收取实际费用20%','2026-05-12 17:42:20','2026-05-12 17:42:20'),(6,'PS-1778578940804-1f5bd46e52',16,NULL,16,'supplier_settlement','platform_to_receiver','platform',NULL,'supplier',3,38400,'CNY','profit_sharing','success','PS-1778578940804-1f5bd46e52','mock-profit-sharing-PS-1778578940804-1f5bd46e52','{\"mock\": true, \"action\": \"profit_sharing\", \"amountFen\": 38400, \"outOrderNo\": \"PS-1778578940804-1f5bd46e52\", \"receiverRole\": \"supplier\"}','正常订单结束，工厂获得实际费用80%','2026-05-12 17:42:20','2026-05-12 17:42:20');
/*!40000 ALTER TABLE `order_fund_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_payment`
--

DROP TABLE IF EXISTS `order_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付记录主键ID',
  `payment_no` varchar(64) NOT NULL COMMENT '系统内部支付流水号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '兼容旧单订单支付；批量支付时为空或为首个订单ID',
  `pay_scene` varchar(32) NOT NULL DEFAULT 'rent_deposit' COMMENT '支付场景：rent_deposit租金加押金 additional补差价 compensation赔付',
  `payment_channel` varchar(32) NOT NULL DEFAULT 'wechat_miniapp' COMMENT '支付渠道：wechat_miniapp微信小程序支付',
  `payment_status` varchar(32) NOT NULL DEFAULT 'created' COMMENT '支付状态：created已创建 paying支付中 success支付成功 fail支付失败 closed已关闭 refunded已退款 partial_refunded部分退款',
  `merchant_mchid` varchar(32) NOT NULL COMMENT '微信支付商户号',
  `appid` varchar(64) NOT NULL COMMENT '微信小程序APPID',
  `openid` varchar(64) NOT NULL COMMENT '支付用户openid快照',
  `merchant_out_trade_no` varchar(64) NOT NULL COMMENT '商户订单号，传给微信支付且必须唯一',
  `wx_transaction_id` varchar(64) DEFAULT NULL COMMENT '微信支付订单号',
  `wx_prepay_id` varchar(128) DEFAULT NULL COMMENT '微信预支付会话标识prepay_id',
  `description` varchar(127) NOT NULL COMMENT '商品描述，对应微信支付下单description',
  `amount_fen` int(11) NOT NULL COMMENT '本次支付金额，单位分',
  `currency` varchar(8) NOT NULL DEFAULT 'CNY' COMMENT '币种，默认人民币CNY',
  `client_ip` varchar(64) DEFAULT NULL COMMENT '支付客户端IP',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '支付通知回调地址快照',
  `attach_data` varchar(255) DEFAULT NULL COMMENT '附加透传数据',
  `time_expire` datetime DEFAULT NULL COMMENT '本次支付失效时间',
  `success_time` datetime DEFAULT NULL COMMENT '微信支付成功时间',
  `fail_reason` varchar(255) DEFAULT NULL COMMENT '支付失败原因',
  `closed_reason` varchar(255) DEFAULT NULL COMMENT '关单原因',
  `raw_create_response` json DEFAULT NULL COMMENT '统一下单原始响应报文',
  `raw_query_response` json DEFAULT NULL COMMENT '查单原始响应报文',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_payment_no` (`payment_no`),
  UNIQUE KEY `uk_order_payment_out_trade_no` (`merchant_out_trade_no`),
  KEY `idx_order_payment_order_id` (`order_id`),
  KEY `idx_order_payment_status` (`payment_status`),
  KEY `idx_order_payment_wx_transaction_id` (`wx_transaction_id`),
  KEY `idx_order_payment_openid` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='订单支付交易表，一笔业务订单可对应多次支付尝试';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_payment`
--

LOCK TABLES `order_payment` WRITE;
/*!40000 ALTER TABLE `order_payment` DISABLE KEYS */;
INSERT INTO `order_payment` VALUES (1,'PAY-1777280670571',1,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-efc359e8524c4d77a797','mock-txn-OUT-efc359e8524c4d77a797','mock-prepay-OUT-efc359e8524c4d77a797','MUHOU 道具租赁订单支付',49000,'CNY','127.0.0.1','','orderId=1','2026-04-27 17:19:31','2026-04-27 17:04:31',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-efc359e8524c4d77a797\"}',NULL,'2026-04-27 17:04:30','2026-04-27 17:04:30'),(2,'PAY-1777280938143',2,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-ec216490a6164b07a8b3','mock-txn-OUT-ec216490a6164b07a8b3','mock-prepay-OUT-ec216490a6164b07a8b3','MUHOU 道具租赁订单支付',8802500,'CNY','127.0.0.1','','orderId=2','2026-04-27 17:23:58','2026-04-27 17:08:58',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-ec216490a6164b07a8b3\"}',NULL,'2026-04-27 17:08:58','2026-04-27 17:08:58'),(3,'PAY-1777282099197',3,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-366521663b544cff8543','mock-txn-OUT-366521663b544cff8543','mock-prepay-OUT-366521663b544cff8543','MUHOU 道具租赁订单支付',8903400,'CNY','127.0.0.1','','orderId=3','2026-04-27 17:43:19','2026-04-27 17:28:19',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-366521663b544cff8543\"}',NULL,'2026-04-27 17:28:19','2026-04-27 17:28:19'),(4,'PAY-1777339438023',4,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-85e0b731d2714c628dcd','mock-txn-OUT-85e0b731d2714c628dcd','mock-prepay-OUT-85e0b731d2714c628dcd','MUHOU 道具租赁订单支付',1220000,'CNY','127.0.0.1','','orderId=4','2026-04-28 09:38:58','2026-04-28 09:23:58',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-85e0b731d2714c628dcd\"}',NULL,'2026-04-28 09:23:58','2026-04-28 09:23:58'),(5,'PAY-1777366166685',5,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-661dd712f67e4e5397a1','mock-txn-OUT-661dd712f67e4e5397a1','mock-prepay-OUT-661dd712f67e4e5397a1','MUHOU 道具租赁订单支付',256200,'CNY','127.0.0.1','','orderId=5','2026-04-28 17:04:27','2026-04-28 16:49:27',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-661dd712f67e4e5397a1\"}',NULL,'2026-04-28 16:49:26','2026-04-28 16:49:26'),(6,'PAY-1777366656944',6,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-46b0c152f47f4e929e65','mock-txn-OUT-46b0c152f47f4e929e65','mock-prepay-OUT-46b0c152f47f4e929e65','MUHOU 道具租赁订单支付',187600,'CNY','127.0.0.1','','orderId=6','2026-04-28 17:12:37','2026-04-28 16:57:37',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-46b0c152f47f4e929e65\"}',NULL,'2026-04-28 16:57:36','2026-04-28 16:57:36'),(7,'PAY-1777446080085',7,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-c4cf142f1b614e45b3cb','mock-txn-OUT-c4cf142f1b614e45b3cb','mock-prepay-OUT-c4cf142f1b614e45b3cb','MUHOU 道具租赁订单支付',161000,'CNY','127.0.0.1','','orderId=7','2026-04-29 15:16:20','2026-04-29 15:01:20',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-c4cf142f1b614e45b3cb\"}',NULL,'2026-04-29 15:01:20','2026-04-29 15:01:20'),(8,'PAY-1777451974080',8,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-08bffffa149243118b51','mock-txn-OUT-08bffffa149243118b51','mock-prepay-OUT-08bffffa149243118b51','MUHOU 道具租赁订单支付',203000,'CNY','127.0.0.1','','orderId=8','2026-04-29 16:54:34','2026-04-29 16:39:34',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-08bffffa149243118b51\"}',NULL,'2026-04-29 16:39:34','2026-04-29 16:39:34'),(9,'PAY-1777452059580',9,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-608fd638c2774ddc840c','mock-txn-OUT-608fd638c2774ddc840c','mock-prepay-OUT-608fd638c2774ddc840c','MUHOU 道具租赁订单支付',154000,'CNY','127.0.0.1','','orderId=9','2026-04-29 16:56:00','2026-04-29 16:41:00',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-608fd638c2774ddc840c\"}',NULL,'2026-04-29 16:40:59','2026-04-29 16:40:59'),(10,'PAY-1777452500353',10,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-46a49daa291b468697cc','mock-txn-OUT-46a49daa291b468697cc','mock-prepay-OUT-46a49daa291b468697cc','MUHOU 道具租赁订单支付',55000,'CNY','127.0.0.1','','orderId=10','2026-04-29 17:03:20','2026-04-29 16:48:20',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-46a49daa291b468697cc\"}',NULL,'2026-04-29 16:48:20','2026-04-29 16:48:20'),(11,'PAY-1777452993059',11,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-d84f2558ee4b418eb136','mock-txn-OUT-d84f2558ee4b418eb136','mock-prepay-OUT-d84f2558ee4b418eb136','MUHOU 道具租赁订单支付',105000,'CNY','127.0.0.1','','orderId=11','2026-04-29 17:11:33','2026-04-29 16:56:33',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-d84f2558ee4b418eb136\"}',NULL,'2026-04-29 16:56:33','2026-04-29 16:56:33'),(12,'PAY-1777465426882',12,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-15669780c8714ea89ee9','mock-txn-OUT-15669780c8714ea89ee9','mock-prepay-OUT-15669780c8714ea89ee9','MUHOU 道具租赁订单支付',175000,'CNY','127.0.0.1','','orderId=12','2026-04-29 20:38:47','2026-04-29 20:23:47',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-15669780c8714ea89ee9\"}',NULL,'2026-04-29 20:23:46','2026-04-29 20:23:46'),(13,'PAY-1777465602432',13,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-6aff799c70874ca48bad','mock-txn-OUT-6aff799c70874ca48bad','mock-prepay-OUT-6aff799c70874ca48bad','MUHOU 道具租赁订单支付',670000,'CNY','127.0.0.1','','orderId=13','2026-04-29 20:41:42','2026-04-29 20:26:42',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-6aff799c70874ca48bad\"}',NULL,'2026-04-29 20:26:42','2026-04-29 20:26:42'),(14,'PAY-1778144536677',14,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-a3510db2f381466eb3c8','mock-txn-OUT-a3510db2f381466eb3c8','mock-prepay-OUT-a3510db2f381466eb3c8','MUHOU 道具租赁订单支付',130000,'CNY','127.0.0.1','','orderId=14','2026-05-07 17:17:17','2026-05-07 17:02:17',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-a3510db2f381466eb3c8\"}',NULL,'2026-05-07 17:02:16','2026-05-07 17:02:16'),(15,'PAY-1778149154665',15,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-99b5fbbe93e4443198f6','mock-txn-OUT-99b5fbbe93e4443198f6','mock-prepay-OUT-99b5fbbe93e4443198f6','MUHOU 道具租赁订单支付',82000,'CNY','127.0.0.1','','orderId=15','2026-05-07 18:34:15','2026-05-07 18:19:15',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-99b5fbbe93e4443198f6\"}',NULL,'2026-05-07 18:19:14','2026-05-07 18:19:14'),(16,'PAY-1778244378162',16,'rent_deposit','wechat_miniapp','success','','demo-miniapp','demo-openid-demander','OUT-ac0dce9952b446e19869','mock-txn-OUT-ac0dce9952b446e19869','mock-prepay-OUT-ac0dce9952b446e19869','MUHOU 道具租赁订单支付',98000,'CNY','127.0.0.1','','orderId=16','2026-05-08 21:01:18','2026-05-08 20:46:18',NULL,NULL,'{\"mock\": true, \"merchantOutTradeNo\": \"OUT-ac0dce9952b446e19869\"}',NULL,'2026-05-08 20:46:18','2026-05-08 20:46:18');
/*!40000 ALTER TABLE `order_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_refund`
--

DROP TABLE IF EXISTS `order_refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_refund` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退款记录主键ID',
  `refund_no` varchar(64) NOT NULL COMMENT '系统内部退款流水号',
  `order_id` bigint(20) NOT NULL COMMENT '关联业务订单ID',
  `payment_id` bigint(20) NOT NULL COMMENT '关联支付记录ID',
  `refund_scene` varchar(32) NOT NULL COMMENT '退款场景：deposit押金退款 timeout_cancel超时取消退款 dispute纠纷退款 manual人工退款',
  `refund_status` varchar(32) NOT NULL DEFAULT 'created' COMMENT '退款状态：created已创建 processing退款中 success退款成功 fail退款失败 closed已关闭 abnormal异常',
  `merchant_mchid` varchar(32) NOT NULL COMMENT '微信支付商户号',
  `appid` varchar(64) NOT NULL COMMENT '微信小程序APPID',
  `merchant_out_refund_no` varchar(64) NOT NULL COMMENT '商户退款单号，调用微信退款接口时必须唯一',
  `wx_refund_id` varchar(64) DEFAULT NULL COMMENT '微信退款单号',
  `merchant_out_trade_no` varchar(64) NOT NULL COMMENT '原支付商户订单号',
  `wx_transaction_id` varchar(64) DEFAULT NULL COMMENT '原微信支付订单号',
  `total_amount_fen` int(11) NOT NULL COMMENT '原支付总金额，单位分',
  `refund_amount_fen` int(11) NOT NULL COMMENT '本次退款金额，单位分',
  `currency` varchar(8) NOT NULL DEFAULT 'CNY' COMMENT '币种，默认人民币CNY',
  `reason` varchar(255) DEFAULT NULL COMMENT '退款原因',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '退款结果通知地址快照',
  `success_time` datetime DEFAULT NULL COMMENT '退款成功时间',
  `fail_reason` varchar(255) DEFAULT NULL COMMENT '退款失败原因',
  `raw_refund_response` json DEFAULT NULL COMMENT '微信退款接口原始响应报文',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_refund_no` (`refund_no`),
  UNIQUE KEY `uk_order_refund_out_refund_no` (`merchant_out_refund_no`),
  KEY `idx_order_refund_order_id` (`order_id`),
  KEY `idx_order_refund_payment_id` (`payment_id`),
  KEY `idx_order_refund_status` (`refund_status`),
  KEY `idx_order_refund_wx_refund_id` (`wx_refund_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单退款记录表，一笔支付可对应多次退款';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_refund`
--

LOCK TABLES `order_refund` WRITE;
/*!40000 ALTER TABLE `order_refund` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_refund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_review`
--

DROP TABLE IF EXISTS `order_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_review` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单评价主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '关联订单ID',
  `reviewer_user_id` bigint(20) NOT NULL COMMENT '评价人用户ID',
  `reviewer_role` varchar(20) NOT NULL COMMENT '评价角色：demander/supplier',
  `score` int(11) NOT NULL COMMENT '评分，建议1-5',
  `prop_score` int(11) DEFAULT NULL COMMENT '道具本身评分：1-5',
  `counterparty_score` int(11) DEFAULT NULL COMMENT '对方评分：1-5',
  `content` varchar(1000) DEFAULT NULL COMMENT '评价内容',
  `visible_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否前台展示：0否 1是',
  `auto_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否系统默认好评：0否 1是',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_reviewer_role` (`order_id`,`reviewer_role`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='订单评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_review`
--

LOCK TABLES `order_review` WRITE;
/*!40000 ALTER TABLE `order_review` DISABLE KEYS */;
INSERT INTO `order_review` VALUES (1,6,1,'demander',5,5,5,'还不错',1,0,'2026-04-28 20:41:49'),(2,6,3,'supplier',5,4,5,'好',1,0,'2026-04-28 21:00:15'),(3,9,3,'supplier',5,5,5,'人不孬',1,0,'2026-04-29 16:43:04'),(4,9,1,'demander',5,5,5,'好',1,0,'2026-04-29 16:43:27'),(7,11,1,'demander',5,5,5,'系统默认好评',1,1,'2026-04-29 16:59:10'),(8,11,3,'supplier',5,5,5,'系统默认好评',1,1,'2026-04-29 16:59:10'),(9,13,1,'demander',4,3,5,'还不错',1,0,'2026-04-29 20:30:00'),(10,13,3,'supplier',4,3,5,'好',0,0,'2026-04-29 20:30:23'),(11,2,1,'demander',5,5,5,'系统默认好评',1,1,'2026-04-30 18:18:51'),(12,2,3,'supplier',5,5,5,'系统默认好评',1,1,'2026-04-30 18:18:51'),(13,3,1,'demander',5,5,5,'系统默认好评',1,1,'2026-04-30 18:18:51'),(14,3,3,'supplier',5,5,5,'系统默认好评',1,1,'2026-04-30 18:18:51'),(15,4,1,'demander',5,5,5,'系统默认好评',1,1,'2026-05-01 22:25:15'),(16,4,3,'supplier',5,5,5,'系统默认好评',1,1,'2026-05-01 22:25:15'),(17,14,1,'demander',5,5,5,'系统默认好评',1,1,'2026-05-10 22:01:07'),(18,14,3,'supplier',5,5,5,'系统默认好评',1,1,'2026-05-10 22:01:08'),(19,15,1,'demander',5,5,5,'系统默认好评',1,1,'2026-05-12 17:42:20'),(20,15,3,'supplier',5,5,5,'系统默认好评',1,1,'2026-05-12 17:42:20'),(21,16,1,'demander',5,5,5,'系统默认好评',1,1,'2026-05-12 17:42:20'),(22,16,3,'supplier',5,5,5,'系统默认好评',1,1,'2026-05-12 17:42:20');
/*!40000 ALTER TABLE `order_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_scan_log`
--

DROP TABLE IF EXISTS `order_scan_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_scan_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '扫码日志主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '关联订单ID',
  `order_item_id` bigint(20) NOT NULL COMMENT '关联订单道具明细ID',
  `prop_id` bigint(20) NOT NULL COMMENT '关联道具ID',
  `scan_type` varchar(20) NOT NULL COMMENT '扫码类型：outbound出库 return归还',
  `scan_code` varchar(128) DEFAULT NULL COMMENT '扫码原始内容',
  `operator_user_id` bigint(20) NOT NULL COMMENT '操作人用户ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '扫码时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单扫码日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_scan_log`
--

LOCK TABLES `order_scan_log` WRITE;
/*!40000 ALTER TABLE `order_scan_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_scan_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_notify_log`
--

DROP TABLE IF EXISTS `payment_notify_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_notify_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付通知日志主键ID',
  `notify_id` varchar(64) NOT NULL COMMENT '微信支付通知唯一ID，用于幂等去重',
  `notify_type` varchar(32) NOT NULL DEFAULT 'payment' COMMENT '通知类型：payment支付通知 refund退款通知',
  `event_type` varchar(64) DEFAULT NULL COMMENT '微信通知事件类型',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '通知资源类型',
  `merchant_mchid` varchar(32) DEFAULT NULL COMMENT '商户号',
  `appid` varchar(64) DEFAULT NULL COMMENT '小程序APPID',
  `wechatpay_serial` varchar(64) DEFAULT NULL COMMENT 'Wechatpay-Serial请求头值',
  `wechatpay_signature` varchar(512) DEFAULT NULL COMMENT 'Wechatpay-Signature请求头值',
  `wechatpay_timestamp` varchar(32) DEFAULT NULL COMMENT 'Wechatpay-Timestamp请求头值',
  `wechatpay_nonce` varchar(64) DEFAULT NULL COMMENT 'Wechatpay-Nonce请求头值',
  `request_headers_json` json DEFAULT NULL COMMENT '回调请求头原始快照',
  `request_body` json NOT NULL COMMENT '回调请求体原始报文',
  `verify_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '验签状态：pending待验签 success成功 fail失败',
  `decrypt_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '解密状态：pending待解密 success成功 fail失败',
  `process_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '业务处理状态：pending待处理 success成功 fail失败 ignored已忽略',
  `payment_no` varchar(64) DEFAULT NULL COMMENT '关联支付流水号',
  `merchant_out_trade_no` varchar(64) DEFAULT NULL COMMENT '关联商户订单号',
  `merchant_out_refund_no` varchar(64) DEFAULT NULL COMMENT '关联商户退款单号',
  `wx_transaction_id` varchar(64) DEFAULT NULL COMMENT '关联微信支付订单号',
  `wx_refund_id` varchar(64) DEFAULT NULL COMMENT '关联微信退款单号',
  `summary` varchar(255) DEFAULT NULL COMMENT '处理摘要信息',
  `error_message` varchar(500) DEFAULT NULL COMMENT '验签、解密或业务处理失败原因',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知接收时间',
  `processed_at` datetime DEFAULT NULL COMMENT '通知处理完成时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_notify_id` (`notify_id`),
  KEY `idx_payment_notify_process_status` (`process_status`),
  KEY `idx_payment_notify_out_trade_no` (`merchant_out_trade_no`),
  KEY `idx_payment_notify_out_refund_no` (`merchant_out_refund_no`),
  KEY `idx_payment_notify_payment_no` (`payment_no`),
  KEY `idx_payment_notify_wx_transaction_id` (`wx_transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信支付异步通知日志表，用于验签、解密、幂等和审计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_notify_log`
--

LOCK TABLES `payment_notify_log` WRITE;
/*!40000 ALTER TABLE `payment_notify_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_notify_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_order_link`
--

DROP TABLE IF EXISTS `payment_order_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_order_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `payment_id` bigint(20) NOT NULL COMMENT '支付流水ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号快照',
  `supplier_user_id` bigint(20) NOT NULL COMMENT '商家用户ID快照',
  `rent_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '该订单租金金额，单位分',
  `deposit_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '该订单押金金额，单位分',
  `total_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '该订单支付分摊总额，单位分',
  `refund_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '该订单已退款金额，单位分',
  `settlement_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '该订单已结算给商家的金额，单位分',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_order_link_payment_order` (`payment_id`,`order_id`),
  KEY `idx_payment_order_link_payment` (`payment_id`),
  KEY `idx_payment_order_link_order` (`order_id`),
  KEY `idx_payment_order_link_supplier` (`supplier_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_order_link`
--

LOCK TABLES `payment_order_link` WRITE;
/*!40000 ALTER TABLE `payment_order_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_order_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_scheme`
--

DROP TABLE IF EXISTS `project_scheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_scheme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '方案主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '所属需求方用户ID',
  `project_name` varchar(128) NOT NULL COMMENT '方案名称',
  `project_description` varchar(500) DEFAULT NULL COMMENT '方案说明',
  `project_status` varchar(20) NOT NULL DEFAULT 'editing' COMMENT '方案状态：editing编辑中 ordered已下单',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `ordered_at` datetime DEFAULT NULL COMMENT '转订单时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COMMENT='方案主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_scheme`
--

LOCK TABLES `project_scheme` WRITE;
/*!40000 ALTER TABLE `project_scheme` DISABLE KEYS */;
INSERT INTO `project_scheme` VALUES (2,23,'方案1776777572348','','editing','2026-04-21 21:19:32','2026-04-21 21:19:32',NULL),(3,1,'新方案0427','这里','ordered','2026-04-27 16:57:40','2026-04-27 17:04:30','2026-04-27 17:04:31'),(4,1,'咋样呀','啦啦啦','ordered','2026-04-27 16:59:58','2026-04-27 17:08:58','2026-04-27 17:08:58'),(5,6,'了','','editing','2026-04-27 17:00:59','2026-04-27 17:00:59',NULL),(6,6,'啦啦啦','','editing','2026-04-27 17:01:20','2026-04-27 17:01:21',NULL),(7,1,'心心心','哈哈哈','ordered','2026-04-27 17:28:01','2026-04-27 17:28:19','2026-04-27 17:28:19'),(8,1,'新的方案0428','说明','ordered','2026-04-28 09:04:19','2026-04-28 09:23:58','2026-04-28 09:23:58'),(9,1,'新的','新的说明','ordered','2026-04-28 11:46:37','2026-04-28 16:49:26','2026-04-28 16:49:27'),(10,1,'动物园','哈哈哈','ordered','2026-04-28 16:56:51','2026-04-28 16:57:36','2026-04-28 16:57:37'),(11,1,'new','','ordered','2026-04-29 14:59:02','2026-04-29 15:01:20','2026-04-29 15:01:20'),(12,1,'1','这里','ordered','2026-04-29 15:01:38','2026-04-29 16:39:34','2026-04-29 16:39:34'),(13,1,'新的方案','','ordered','2026-04-29 16:40:46','2026-04-29 16:40:59','2026-04-29 16:41:00'),(14,1,'111','','ordered','2026-04-29 16:46:59','2026-04-29 16:56:33','2026-04-29 16:56:33'),(15,1,'哈哈哈','','ordered','2026-04-29 16:48:08','2026-04-29 16:48:20','2026-04-29 16:48:20'),(16,1,'新方案2026','你好','ordered','2026-04-29 20:21:36','2026-04-29 20:23:46','2026-04-29 20:23:47'),(17,1,'冲突方案','11','ordered','2026-04-29 20:22:42','2026-04-29 20:26:42','2026-04-29 20:26:42'),(18,1,'测试','','ordered','2026-04-29 20:55:35','2026-05-07 17:02:16','2026-05-07 17:02:17'),(19,1,'测试2','','ordered','2026-04-29 20:55:54','2026-05-07 18:19:14','2026-05-07 18:19:15'),(20,1,'新方案','新方案','ordered','2026-05-08 20:45:07','2026-05-08 20:46:18','2026-05-08 20:46:18');
/*!40000 ALTER TABLE `project_scheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_scheme_item`
--

DROP TABLE IF EXISTS `project_scheme_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_scheme_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '方案道具明细主键ID',
  `project_id` bigint(20) NOT NULL COMMENT '关联方案ID',
  `prop_id` bigint(20) NOT NULL COMMENT '关联道具ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入方案时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_prop` (`project_id`,`prop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COMMENT='方案道具明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_scheme_item`
--

LOCK TABLES `project_scheme_item` WRITE;
/*!40000 ALTER TABLE `project_scheme_item` DISABLE KEYS */;
INSERT INTO `project_scheme_item` VALUES (7,2,6,'2026-04-21 21:19:32'),(9,2,2,'2026-04-21 21:19:49'),(11,3,119,'2026-04-27 17:02:36'),(12,3,118,'2026-04-27 17:02:39'),(17,4,123,'2026-04-27 17:08:39'),(18,7,122,'2026-04-27 17:28:01'),(19,7,123,'2026-04-27 17:28:04'),(21,7,121,'2026-04-27 17:28:08'),(23,8,127,'2026-04-28 09:23:41'),(24,9,128,'2026-04-28 11:47:41'),(27,9,127,'2026-04-28 16:47:56'),(28,10,128,'2026-04-28 16:56:52'),(29,10,127,'2026-04-28 16:56:54'),(30,11,129,'2026-04-29 14:59:02'),(32,11,128,'2026-04-29 14:59:45'),(33,12,129,'2026-04-29 15:01:38'),(34,12,128,'2026-04-29 16:39:10'),(36,13,129,'2026-04-29 16:40:46'),(37,13,128,'2026-04-29 16:40:48'),(38,14,129,'2026-04-29 16:46:59'),(39,14,128,'2026-04-29 16:47:02'),(40,15,129,'2026-04-29 16:48:08'),(41,16,131,'2026-04-29 20:21:36'),(42,16,129,'2026-04-29 20:22:04'),(43,17,131,'2026-04-29 20:22:42'),(44,17,128,'2026-04-29 20:22:44'),(52,18,133,'2026-05-07 17:00:44'),(53,19,133,'2026-05-07 18:18:52'),(54,20,133,'2026-05-08 20:45:07');
/*!40000 ALTER TABLE `project_scheme_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prop_audit`
--

DROP TABLE IF EXISTS `prop_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prop_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '道具审核主键ID',
  `prop_id` bigint(20) NOT NULL COMMENT '关联道具ID',
  `action_type` varchar(20) NOT NULL COMMENT '审核动作：create新增 update修改 up上架 down下架',
  `audit_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '审核状态：pending待审核 approved通过 rejected驳回',
  `apply_remark` varchar(255) DEFAULT NULL COMMENT '申请说明',
  `audit_remark` varchar(255) DEFAULT NULL COMMENT '审核说明',
  `reviewer_id` bigint(20) DEFAULT NULL COMMENT '审核人用户ID',
  `submitted_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `reviewed_at` datetime DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COMMENT='道具审核记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prop_audit`
--

LOCK TABLES `prop_audit` WRITE;
/*!40000 ALTER TABLE `prop_audit` DISABLE KEYS */;
INSERT INTO `prop_audit` VALUES (3,122,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-23 11:40:44','2026-04-24 22:13:39'),(4,122,'up','approved','工厂端提交了上下架申请','',3,'2026-04-24 21:28:47','2026-04-24 22:14:02'),(5,122,'down','approved','工厂端提交了上下架申请','',3,'2026-04-24 21:28:48','2026-04-24 22:13:59'),(6,121,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-24 21:29:31','2026-04-24 22:13:54'),(7,122,'down','rejected','工厂端提交了下架申请','',3,'2026-04-24 22:16:56','2026-04-24 23:22:51'),(8,121,'down','approved','工厂端提交了下架申请','',3,'2026-04-24 22:17:48','2026-04-24 23:23:02'),(9,122,'down','approved','工厂端提交了下架申请','',3,'2026-04-24 23:23:19','2026-04-27 16:17:13'),(10,123,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-24 23:27:54','2026-04-27 16:17:09'),(11,122,'up','approved','工厂端提交了上架申请','',3,'2026-04-27 16:24:45','2026-04-27 16:25:03'),(12,122,'down','rejected','工厂端提交了下架申请','',3,'2026-04-27 16:25:11','2026-04-27 16:25:21'),(13,122,'down','rejected','工厂端提交了下架申请','',3,'2026-04-27 16:32:11','2026-04-27 17:27:39'),(14,121,'up','approved','工厂端提交了上架申请','',3,'2026-04-27 16:32:25','2026-04-27 17:27:35'),(15,123,'down','rejected','工厂端提交了下架申请','',3,'2026-04-27 17:31:57','2026-04-27 17:36:41'),(16,124,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-27 17:34:05','2026-04-27 17:34:42'),(17,125,'create','rejected','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-27 17:37:27','2026-04-27 17:37:40'),(18,125,'up','rejected','工厂端提交了上架申请','',3,'2026-04-27 17:37:48','2026-04-27 17:49:45'),(19,126,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-27 17:49:02','2026-04-27 17:50:57'),(20,127,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-28 09:19:29','2026-04-28 09:19:47'),(21,128,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-28 11:45:24','2026-04-28 11:45:51'),(22,129,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-29 14:50:45','2026-04-29 14:52:14'),(23,129,'down','approved','工厂端提交了下架申请','',3,'2026-04-29 14:54:13','2026-04-29 14:55:11'),(24,129,'up','approved','工厂端提交了上架申请','',3,'2026-04-29 14:55:28','2026-04-29 14:55:47'),(25,131,'create','rejected','工厂补全扫码入库资料，待管理员确认','',3,'2026-04-29 20:15:43','2026-04-29 20:16:33'),(26,131,'up','approved','工厂端提交了上架申请','',3,'2026-04-29 20:16:49','2026-04-29 20:17:02'),(27,125,'up','approved','工厂端提交了上架申请','',3,'2026-04-29 20:19:54','2026-04-29 20:20:10'),(28,133,'create','approved','工厂补全扫码入库资料，待管理员确认','',3,'2026-05-07 11:22:57','2026-05-07 17:00:33');
/*!40000 ALTER TABLE `prop_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prop_image`
--

DROP TABLE IF EXISTS `prop_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prop_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '道具图片主键ID',
  `prop_id` bigint(20) NOT NULL COMMENT '关联道具ID',
  `image_url` varchar(500) NOT NULL COMMENT '图片地址',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序号，越小越靠前',
  `main_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否首图：0否 1是',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_prop_image_prop_sort` (`prop_id`,`sort_order`,`id`),
  KEY `idx_prop_image_prop_main` (`prop_id`,`main_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COMMENT='道具图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prop_image`
--

LOCK TABLES `prop_image` WRITE;
/*!40000 ALTER TABLE `prop_image` DISABLE KEYS */;
INSERT INTO `prop_image` VALUES (1,101,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(2,102,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(3,103,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(4,104,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(5,105,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(6,106,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(7,107,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(8,108,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(9,109,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(10,110,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(11,111,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(12,112,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(13,113,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(14,114,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(15,115,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(16,116,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(17,117,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(18,118,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(19,119,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(20,120,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(21,121,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(22,122,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(23,123,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(24,124,'wxfile://tmp_27aedb7048bdf80132d9a38536a25ccd.jpg',0,1,'2026-04-28 11:37:50'),(25,125,'/images/stage-prop-real.jpg',0,1,'2026-04-28 11:37:50'),(26,126,'wxfile://tmp_c91eceda81f92154edcd268e82be9b35.jpg',0,1,'2026-04-28 11:37:50'),(27,127,'wxfile://tmp_e2219a15c4f496a6ae756a5e967b1b6c.jpg',0,1,'2026-04-28 11:37:50'),(33,128,'wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',0,1,'2026-04-28 11:45:24'),(34,128,'wxfile://tmp_4d73a92a1b3775976eba0e138885f650.jpg',1,0,'2026-04-28 11:45:24'),(35,128,'wxfile://tmp_7df357cb1687c82386873738918ea743.jpg',2,0,'2026-04-28 11:45:24'),(36,128,'wxfile://tmp_a35020b02d1a24775eaa04790739a389.jpg',3,0,'2026-04-28 11:45:24'),(38,129,'/images/stage-prop-real.jpg',0,1,'2026-04-29 14:50:45'),(39,130,'/images/stage-prop-real.jpg',0,1,'2026-04-29 15:17:45'),(41,131,'wxfile://tmp_5cbee36ea9e9eafdca308f864286aba3.jpg',0,1,'2026-04-29 20:15:43'),(42,131,'wxfile://tmp_64003e3cab7f3e5700e527753ad13f30.jpg',1,0,'2026-04-29 20:15:43'),(43,132,'/images/stage-prop-real.jpg',0,1,'2026-05-05 22:09:25'),(45,134,'/images/stage-prop-real.jpg',0,1,'2026-05-07 09:01:47'),(46,133,'wxfile://tmp_f90f6413970b32206eda902a8f5a26ec.jpg',0,1,'2026-05-07 11:22:57'),(47,133,'wxfile://tmp_a082cbdc23a9b1417f9e21d83dbdae0b.jpg',1,0,'2026-05-07 11:22:57');
/*!40000 ALTER TABLE `prop_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prop_info`
--

DROP TABLE IF EXISTS `prop_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prop_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '道具主键ID',
  `supplier_user_id` bigint(20) DEFAULT NULL COMMENT '所属工厂用户ID，管理员生成二维码时为空，工厂扫码录入后再绑定',
  `prop_name` varchar(128) DEFAULT NULL COMMENT '道具名称，管理员预生成二维码时可暂为空',
  `image_url` varchar(255) DEFAULT NULL COMMENT '道具图片地址',
  `style_code` varchar(32) DEFAULT NULL COMMENT '风格编码，如 modern、traditional、tech',
  `type_code` varchar(32) DEFAULT NULL COMMENT '类型编码，如 light、audio、screen、structure',
  `size_desc` varchar(128) DEFAULT NULL COMMENT '尺寸补充描述，如异形、折叠后尺寸、展开后尺寸',
  `length_cm` decimal(10,2) DEFAULT NULL COMMENT '长度，单位厘米',
  `width_cm` decimal(10,2) DEFAULT NULL COMMENT '宽度，单位厘米',
  `height_cm` decimal(10,2) DEFAULT NULL COMMENT '高度，单位厘米',
  `material_desc` varchar(128) DEFAULT NULL COMMENT '材质说明',
  `daily_rent_price_fen` int(11) NOT NULL DEFAULT '0' COMMENT '日租金，单位分',
  `deposit_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '押金金额，单位分',
  `fire_resistant_option` varchar(32) DEFAULT NULL COMMENT '是否阻燃：是/否/可喷阻燃液',
  `weight_desc` varchar(255) DEFAULT NULL COMMENT '重量描述，如 25kg / 80kg',
  `transport_suggestion` varchar(128) DEFAULT NULL COMMENT '运输建议',
  `prop_status` varchar(20) NOT NULL DEFAULT 'idle' COMMENT '道具状态：idle空闲 locked已锁定 renting租赁中 offline已下架',
  `audit_status` varchar(20) NOT NULL DEFAULT 'approved' COMMENT '审核状态：pending待审核 approved通过 rejected驳回',
  `qr_code_id` varchar(64) DEFAULT NULL COMMENT '二维码唯一标识',
  `qr_code_url` varchar(255) DEFAULT NULL COMMENT '二维码图片地址',
  `fill_status` varchar(20) NOT NULL DEFAULT 'filled' COMMENT '资料填写状态：pending_fill待补充 filled已补全',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prop_qr_code_id` (`qr_code_id`),
  KEY `idx_prop_supplier_status` (`supplier_user_id`,`prop_status`),
  KEY `idx_prop_style_type_status` (`style_code`,`type_code`,`prop_status`),
  KEY `idx_prop_price` (`daily_rent_price_fen`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8mb4 COMMENT='道具信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prop_info`
--

LOCK TABLES `prop_info` WRITE;
/*!40000 ALTER TABLE `prop_info` DISABLE KEYS */;
INSERT INTO `prop_info` VALUES (101,2,'舞台追光灯 A01','/images/stage-prop-real.jpg','现代','灯光','120cm x 80cm x 70cm',120.00,80.00,70.00,'金属',12000,40000,'B1','防震运输','厢式货车','idle','approved','QR-DEMO-0101',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(102,2,'舞台追光灯 A02','/images/stage-prop-real.jpg','现代','灯光','120cm x 80cm x 70cm',120.00,80.00,70.00,'金属',12000,40000,'B1','防震运输','厢式货车','idle','approved','QR-DEMO-0102',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(103,2,'舞台染色灯 B01','/images/stage-prop-real.jpg','现代','灯光','90cm x 60cm x 60cm',90.00,60.00,60.00,'金属',9000,30000,'B1','防震运输','厢式货车','idle','approved','QR-DEMO-0103',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(104,2,'舞台染色灯 B02','/images/stage-prop-real.jpg','现代','灯光','90cm x 60cm x 60cm',90.00,60.00,60.00,'金属',9000,30000,'B1','防震运输','厢式货车','idle','approved','QR-DEMO-0104',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(105,2,'专业音箱 C01','/images/stage-prop-real.jpg','现代','音响','85cm x 55cm x 50cm',85.00,55.00,50.00,'工程塑料+金属',15000,50000,'B1','立放固定','中型货车','idle','approved','QR-DEMO-0105',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(106,2,'专业音箱 C02','/images/stage-prop-real.jpg','现代','音响','85cm x 55cm x 50cm',85.00,55.00,50.00,'工程塑料+金属',15000,50000,'B1','立放固定','中型货车','idle','approved','QR-DEMO-0106',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(107,2,'无线麦克风套装 D01','/images/stage-prop-real.jpg','现代','音响','手持套装',25.00,18.00,10.00,'工程塑料',6000,20000,'B1','箱体运输','小型货车','idle','approved','QR-DEMO-0107',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(108,2,'无线麦克风套装 D02','/images/stage-prop-real.jpg','现代','音响','手持套装',25.00,18.00,10.00,'工程塑料',6000,20000,'B1','箱体运输','小型货车','idle','approved','QR-DEMO-0108',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(109,2,'LED 屏幕模组 E01','/images/stage-prop-real.jpg','科技','屏幕','200cm x 120cm x 18cm',200.00,120.00,18.00,'LED+金属',20000,80000,'B1','木箱运输','厢式货车','idle','approved','QR-DEMO-0109',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(110,2,'LED 屏幕模组 E02','/images/stage-prop-real.jpg','科技','屏幕','200cm x 120cm x 18cm',200.00,120.00,18.00,'LED+金属',20000,80000,'B1','木箱运输','厢式货车','idle','approved','QR-DEMO-0110',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(111,2,'舞台桁架 F01','/images/stage-prop-real.jpg','工业','结构','300cm x 40cm x 40cm',300.00,40.00,40.00,'铝合金',10000,35000,'A','捆扎运输','平板车','idle','approved','QR-DEMO-0111',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(112,2,'舞台桁架 F02','/images/stage-prop-real.jpg','工业','结构','300cm x 40cm x 40cm',300.00,40.00,40.00,'铝合金',10000,35000,'A','捆扎运输','平板车','idle','approved','QR-DEMO-0112',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(113,2,'背景幕布 G01','/images/stage-prop-real.jpg','传统','布景','500cm x 300cm x 8cm',500.00,300.00,8.00,'阻燃布',8000,25000,'B1','卷装运输','小型货车','idle','approved','QR-DEMO-0113',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(114,2,'背景幕布 G02','/images/stage-prop-real.jpg','传统','布景','500cm x 300cm x 8cm',500.00,300.00,8.00,'阻燃布',8000,25000,'B1','卷装运输','小型货车','idle','approved','QR-DEMO-0114',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(115,2,'舞台升降台 H01','/images/stage-prop-real.jpg','现代','结构','220cm x 140cm x 60cm',220.00,140.00,60.00,'钢制结构',18000,70000,'A','平稳装卸','平板车','idle','approved','QR-DEMO-0115',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(116,2,'舞台升降台 H02','/images/stage-prop-real.jpg','现代','结构','220cm x 140cm x 60cm',220.00,140.00,60.00,'钢制结构',18000,70000,'A','平稳装卸','平板车','idle','approved','QR-DEMO-0116',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(117,2,'主持台 I01','/images/stage-prop-real.jpg','现代','道具','110cm x 60cm x 110cm',110.00,60.00,110.00,'木质+金属',5000,15000,'B1','防磕碰运输','小型货车','idle','approved','QR-DEMO-0117',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(118,2,'主持台 I02','/images/stage-prop-real.jpg','现代','道具','110cm x 60cm x 110cm',110.00,60.00,110.00,'木质+金属',5000,15000,'B1','防磕碰运输','小型货车','idle','approved','QR-DEMO-0118',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-27 18:04:43'),(119,2,'舞台效果机 J01','/images/stage-prop-real.jpg','科技','特效','70cm x 45cm x 50cm',70.00,45.00,50.00,'金属',7000,22000,'B1','防倾倒运输','小型货车','idle','approved','QR-DEMO-0119',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-27 18:04:43'),(120,2,'舞台效果机 J02','/images/stage-prop-real.jpg','科技','特效','70cm x 45cm x 50cm',70.00,45.00,50.00,'金属',7000,22000,'B1','防倾倒运输','小型货车','idle','approved','QR-DEMO-0120',NULL,'filled','工厂演示道具','2026-04-22 11:19:12','2026-04-22 15:33:20'),(121,3,'0424','/images/stage-prop-real.jpg','古风','景片模组','1',1.00,1.00,1.00,'没',13100,87500,'是','15','没','idle','approved','QR-20260422165631-5981','QR-20260422165631-5981','filled','工厂扫码录入并提交审核','2026-04-22 16:56:31','2026-04-27 17:28:53'),(122,3,'吧','/images/stage-prop-real.jpg','古风','舞台道具','吧',506.00,20.00,60.00,'吧',100,200,'可喷阻燃液','30','用车','idle','approved','QR-20260422170015-6744','QR-20260422170015-6744','filled','工厂扫码录入并绑定到当前工厂账号','2026-04-22 17:00:15','2026-04-27 17:31:02'),(123,3,'新上架','/images/stage-prop-real.jpg','欧式','布景道具','了',6.00,7.00,4.00,'了',13100,8789400,'否','555','没有','idle','approved','QR-20260423114224-4598','QR-20260423114224-4598','filled','工厂扫码录入并提交审核','2026-04-23 11:42:24','2026-04-27 17:36:41'),(124,3,'新道具0427','wxfile://tmp_27aedb7048bdf80132d9a38536a25ccd.jpg','现代','幕布软景','1',1.00,1.00,1.00,'吧',100,1200,'否','1','1','idle','approved','QR-20260427173234-9808','QR-20260427173234-9808','filled','工厂扫码录入并提交审核','2026-04-27 17:32:34','2026-04-27 17:34:42'),(125,3,'拨回的','/images/stage-prop-real.jpg','现代','舞台道具','2',3.00,231.00,1.00,'了',1300,400,'可喷阻燃液','66','🌧️','idle','approved','QR-20260427173646-1058','QR-20260427173646-1058','filled','工厂扫码录入并提交审核','2026-04-27 17:36:46','2026-04-29 20:20:10'),(126,6,'🐒','wxfile://tmp_c91eceda81f92154edcd268e82be9b35.jpg','古风','幕布软景','没有',1.00,23.00,45.00,'2',5000,50000,'可喷阻燃液','1','宇','idle','approved','QR-20260427174731-3361','QR-20260427174731-3361','filled','工厂扫码录入并提交审核','2026-04-27 17:47:31','2026-04-27 17:50:57'),(127,3,'🐮','wxfile://tmp_e2219a15c4f496a6ae756a5e967b1b6c.jpg','古风','布景道具','牛',12.00,23.00,34.00,'🥩',7800,50000,'可喷阻燃液','500','骑行','idle','approved','QR-20260428091826-1062','QR-20260428091826-1062','filled','工厂扫码录入并提交审核','2026-04-28 09:18:26','2026-04-28 17:01:08'),(128,3,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg','欧式','布景道具','1',2.00,2.00,22.00,'鸡',2000,20000,'是','10','🏃','idle','approved','QR-20260428114413-2633','QR-20260428114413-2633','filled','工厂扫码录入并提交审核','2026-04-28 11:44:13','2026-04-29 20:27:32'),(129,3,'🐯','/images/stage-prop-real.jpg','现代','舞台道具','1',5.00,5.00,5.00,'🥩',5000,50000,'可喷阻燃液','777','1','idle','approved','QR-20260429144812-9018','QR-20260429144812-9018','filled','工厂扫码录入并提交审核','2026-04-29 14:48:12','2026-04-29 20:26:00'),(130,NULL,'待扫码录入道具','/images/stage-prop-real.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'offline','pending','QR-20260429151745-8405','QR-20260429151745-8405','revoked','管理员已作废该二维码','2026-04-29 15:17:45','2026-05-05 22:09:23'),(131,3,'🥵','wxfile://tmp_5cbee36ea9e9eafdca308f864286aba3.jpg','现代','舞台道具','尺寸',1.00,1.00,1.00,'材质',10000,50000,'可喷阻燃液','20','无','idle','approved','QR-20260429201403-9881','QR-20260429201403-9881','filled','工厂扫码录入并提交审核','2026-04-29 20:14:03','2026-04-29 20:28:30'),(132,NULL,'待扫码录入道具','/images/stage-prop-real.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'offline','pending','QR-20260505220925-8b9d6d60','/api/qrcodes/QR-20260505220925-8b9d6d60/image','revoked','管理员已作废该二维码','2026-05-05 22:09:25','2026-05-05 22:39:25'),(133,3,'古风屏风','wxfile://tmp_f90f6413970b32206eda902a8f5a26ec.jpg','古风','景片模组','三折屏风 240cm*180cm',240.00,8.00,180.00,'木框布面',8000,50000,'可喷阻燃液','28','厢式货车竖放固定','idle','approved','QR-20260505223920-1d3bc6b2','/api/qrcodes/QR-20260505223920-1d3bc6b2/image','filled','工厂扫码录入并提交审核','2026-05-05 22:39:20','2026-05-08 20:48:47'),(134,NULL,'待扫码录入道具','/images/stage-prop-real.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'offline','pending','QR-20260507090147-12650a3c','/api/qrcodes/QR-20260507090147-12650a3c/image','pending_fill','管理员预生成小程序码，待工厂扫码录入后再绑定归属','2026-05-07 09:01:47','2026-05-07 09:01:48');
/*!40000 ALTER TABLE `prop_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prop_qr_code`
--

DROP TABLE IF EXISTS `prop_qr_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prop_qr_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `prop_id` bigint(20) NOT NULL COMMENT '关联道具ID',
  `qr_code_id` varchar(64) NOT NULL COMMENT '二维码业务标识',
  `qr_scene` varchar(128) NOT NULL COMMENT '小程序码scene参数',
  `qr_page` varchar(128) NOT NULL COMMENT '小程序码打开页面',
  `qr_image_url` varchar(255) DEFAULT NULL COMMENT '二维码图片访问地址',
  `qr_image_storage_key` varchar(255) DEFAULT NULL COMMENT '二维码图片存储key',
  `image_sha256` varchar(64) DEFAULT NULL COMMENT '二维码图片SHA256摘要',
  `status` varchar(20) NOT NULL DEFAULT 'unused' COMMENT '二维码状态：unused未登记 filled已登记 revoked已作废',
  `printed_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已打印：0否 1是',
  `printed_at` datetime DEFAULT NULL COMMENT '打印时间',
  `downloaded_at` datetime DEFAULT NULL COMMENT '最近下载/查看时间',
  `created_by_admin_user_id` bigint(20) DEFAULT NULL COMMENT '生成二维码的管理员用户ID',
  `used_by_supplier_user_id` bigint(20) DEFAULT NULL COMMENT '扫码登记的工厂用户ID',
  `used_at` datetime DEFAULT NULL COMMENT '登记完成时间',
  `revoked_by_admin_user_id` bigint(20) DEFAULT NULL COMMENT '作废管理员用户ID',
  `revoked_at` datetime DEFAULT NULL COMMENT '作废时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prop_qr_code_id` (`qr_code_id`),
  UNIQUE KEY `uk_prop_qr_prop_id` (`prop_id`),
  KEY `idx_prop_qr_status` (`status`),
  KEY `idx_prop_qr_created_admin` (`created_by_admin_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COMMENT='道具二维码生命周期表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prop_qr_code`
--

LOCK TABLES `prop_qr_code` WRITE;
/*!40000 ALTER TABLE `prop_qr_code` DISABLE KEYS */;
INSERT INTO `prop_qr_code` VALUES (1,101,'QR-DEMO-0101','q=QR-DEMO-0101','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(2,102,'QR-DEMO-0102','q=QR-DEMO-0102','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(3,103,'QR-DEMO-0103','q=QR-DEMO-0103','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(4,104,'QR-DEMO-0104','q=QR-DEMO-0104','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(5,105,'QR-DEMO-0105','q=QR-DEMO-0105','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(6,106,'QR-DEMO-0106','q=QR-DEMO-0106','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(7,107,'QR-DEMO-0107','q=QR-DEMO-0107','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(8,108,'QR-DEMO-0108','q=QR-DEMO-0108','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(9,109,'QR-DEMO-0109','q=QR-DEMO-0109','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(10,110,'QR-DEMO-0110','q=QR-DEMO-0110','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(11,111,'QR-DEMO-0111','q=QR-DEMO-0111','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(12,112,'QR-DEMO-0112','q=QR-DEMO-0112','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(13,113,'QR-DEMO-0113','q=QR-DEMO-0113','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(14,114,'QR-DEMO-0114','q=QR-DEMO-0114','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(15,115,'QR-DEMO-0115','q=QR-DEMO-0115','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(16,116,'QR-DEMO-0116','q=QR-DEMO-0116','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(17,117,'QR-DEMO-0117','q=QR-DEMO-0117','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(18,118,'QR-DEMO-0118','q=QR-DEMO-0118','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-27 18:04:43',NULL,NULL,'2026-04-22 11:19:12','2026-04-27 18:04:43'),(19,119,'QR-DEMO-0119','q=QR-DEMO-0119','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-27 18:04:43',NULL,NULL,'2026-04-22 11:19:12','2026-04-27 18:04:43'),(20,120,'QR-DEMO-0120','q=QR-DEMO-0120','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,2,'2026-04-22 15:33:20',NULL,NULL,'2026-04-22 11:19:12','2026-04-22 15:33:20'),(21,121,'QR-20260422165631-5981','q=QR-20260422165631-5981','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-27 17:28:53',NULL,NULL,'2026-04-22 16:56:31','2026-04-27 17:28:53'),(22,122,'QR-20260422170015-6744','q=QR-20260422170015-6744','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-27 17:31:02',NULL,NULL,'2026-04-22 17:00:15','2026-04-27 17:31:02'),(23,123,'QR-20260423114224-4598','q=QR-20260423114224-4598','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-27 17:36:41',NULL,NULL,'2026-04-23 11:42:24','2026-04-27 17:36:41'),(24,124,'QR-20260427173234-9808','q=QR-20260427173234-9808','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-27 17:34:42',NULL,NULL,'2026-04-27 17:32:34','2026-04-27 17:34:42'),(25,125,'QR-20260427173646-1058','q=QR-20260427173646-1058','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-29 20:20:10',NULL,NULL,'2026-04-27 17:36:46','2026-04-29 20:20:10'),(26,126,'QR-20260427174731-3361','q=QR-20260427174731-3361','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,6,'2026-04-27 17:50:57',NULL,NULL,'2026-04-27 17:47:31','2026-04-27 17:50:57'),(27,127,'QR-20260428091826-1062','q=QR-20260428091826-1062','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-28 17:01:08',NULL,NULL,'2026-04-28 09:18:26','2026-04-28 17:01:08'),(28,128,'QR-20260428114413-2633','q=QR-20260428114413-2633','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-29 20:27:32',NULL,NULL,'2026-04-28 11:44:13','2026-04-29 20:27:32'),(29,129,'QR-20260429144812-9018','q=QR-20260429144812-9018','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-29 20:26:00',NULL,NULL,'2026-04-29 14:48:12','2026-04-29 20:26:00'),(30,130,'QR-20260429151745-8405','q=QR-20260429151745-8405','pages/qr-entry/index',NULL,NULL,NULL,'revoked',0,NULL,NULL,NULL,NULL,NULL,3,'2026-05-05 22:09:23','2026-04-29 15:17:45','2026-05-05 22:09:23'),(31,131,'QR-20260429201403-9881','q=QR-20260429201403-9881','pages/qr-entry/index',NULL,NULL,NULL,'filled',0,NULL,NULL,NULL,3,'2026-04-29 20:28:30',NULL,NULL,'2026-04-29 20:14:03','2026-04-29 20:28:30'),(32,132,'QR-20260505220925-8b9d6d60','q=QR-20260505220925-8b9d6d60','pages/qr-entry/index','/api/qrcodes/QR-20260505220925-8b9d6d60/image','D:\\java\\SourceTreeData\\muhou-backend\\uploads\\qrcode\\QR-20260505220925-8b9d6d60.png','73a2ec422dd3dd7c93010bf45057884c56c38f1ad92467e5ed08620eb3a0ff84','revoked',0,NULL,NULL,3,NULL,NULL,3,'2026-05-05 22:39:25','2026-05-05 22:09:27','2026-05-05 22:39:25'),(33,133,'QR-20260505223920-1d3bc6b2','q=QR-20260505223920-1d3bc6b2','pages/qr-entry/index','/api/qrcodes/QR-20260505223920-1d3bc6b2/image','D:\\java\\SourceTreeData\\muhou-backend\\uploads\\qrcode\\QR-20260505223920-1d3bc6b2.png','0bd339dd01ba94cfc7a1b5731ab00180316ad6d2c46a216472324c13c9a9f7b1','filled',0,NULL,'2026-05-07 09:04:25',3,3,'2026-05-07 11:22:57',NULL,NULL,'2026-05-05 22:39:21','2026-05-07 11:22:57'),(34,134,'QR-20260507090147-12650a3c','q=QR-20260507090147-12650a3c','pages/qr-entry/index','/api/qrcodes/QR-20260507090147-12650a3c/image','D:\\java\\SourceTreeData\\muhou-backend\\uploads\\qrcode\\QR-20260507090147-12650a3c.png','31a85f4232bc57a237c55204a7cce020e7a4752dc531f4630859a91237e91598','unused',0,NULL,'2026-05-07 09:01:50',3,NULL,NULL,NULL,NULL,'2026-05-07 09:01:48','2026-05-07 09:01:50');
/*!40000 ALTER TABLE `prop_qr_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental_order`
--

DROP TABLE IF EXISTS `rental_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rental_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单主键ID',
  `order_no` varchar(64) NOT NULL COMMENT '业务订单编号',
  `project_id` bigint(20) NOT NULL COMMENT '来源方案ID',
  `demander_user_id` bigint(20) NOT NULL COMMENT '需求方用户ID',
  `supplier_user_id` bigint(20) NOT NULL COMMENT '工厂方用户ID',
  `order_status` varchar(32) NOT NULL COMMENT '订单状态：pending_factory_confirm待工厂确认 wait_pickup待取货 renting租赁中 wait_review待评价 completed已完成 cancelled_timeout超时取消 cancelled_manual人工取消',
  `rental_days` int(11) NOT NULL DEFAULT '1' COMMENT '租赁天数',
  `rental_start_date` date DEFAULT NULL COMMENT '租赁开始日期',
  `rental_end_date` date DEFAULT NULL COMMENT '租赁结束日期',
  `contact_address` varchar(255) DEFAULT NULL COMMENT '租赁方本次订单收货/使用地址',
  `use_scene` varchar(100) DEFAULT NULL COMMENT '本次租赁使用场景',
  `special_remark` varchar(500) DEFAULT NULL COMMENT '本次订单特殊用途或备注',
  `rent_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '租金总额，单位分',
  `deposit_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '押金总额，单位分',
  `total_amount_fen` int(11) NOT NULL DEFAULT '0' COMMENT '订单应付总额，单位分',
  `pay_status` varchar(20) NOT NULL DEFAULT 'unpaid' COMMENT '支付状态：unpaid未支付 paying支付中 paid已支付 closed已关闭 failed支付失败',
  `refund_status` varchar(20) NOT NULL DEFAULT 'none' COMMENT '退款状态：none无退款 processing退款中 partial部分退款 full全额退款 failed退款失败',
  `current_payment_id` bigint(20) DEFAULT NULL COMMENT '当前有效支付记录ID，关联 order_payment.id',
  `total_paid_fen` int(11) NOT NULL DEFAULT '0' COMMENT '累计已支付金额，单位分',
  `total_refunded_fen` int(11) NOT NULL DEFAULT '0' COMMENT '累计已退款金额，单位分',
  `confirm_deadline_at` datetime DEFAULT NULL COMMENT '工厂确认截止时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `paid_at` datetime DEFAULT NULL COMMENT '订单支付完成时间',
  `confirmed_at` datetime DEFAULT NULL COMMENT '工厂确认接单时间',
  `picked_up_at` datetime DEFAULT NULL COMMENT '全部出库完成时间',
  `returned_at` datetime DEFAULT NULL COMMENT '全部归还完成时间',
  `reviewed_at` datetime DEFAULT NULL COMMENT '用户评价完成时间',
  `cancelled_at` datetime DEFAULT NULL COMMENT '订单取消时间',
  `cancel_reason` varchar(255) DEFAULT NULL COMMENT '订单取消原因',
  `deposit_refunded_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '押金是否已退回：0否 1是',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `created_by` bigint(20) DEFAULT NULL COMMENT '订单创建人用户ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rental_order_no` (`order_no`),
  KEY `idx_rental_order_demander` (`demander_user_id`,`created_at`),
  KEY `idx_rental_order_supplier_status` (`supplier_user_id`,`order_status`,`created_at`),
  KEY `idx_rental_order_status_deadline` (`order_status`,`confirm_deadline_at`),
  KEY `idx_rental_order_project_id` (`project_id`),
  KEY `idx_rental_order_current_payment_id` (`current_payment_id`),
  KEY `idx_rental_order_status_returned_at` (`order_status`,`returned_at`),
  KEY `idx_rental_order_rental_date` (`rental_start_date`,`rental_end_date`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='租赁订单主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental_order`
--

LOCK TABLES `rental_order` WRITE;
/*!40000 ALTER TABLE `rental_order` DISABLE KEYS */;
INSERT INTO `rental_order` VALUES (1,'ORD-1777280670508',3,1,2,'cancelled_timeout',1,'2026-04-27','2026-04-27',NULL,NULL,NULL,12000,37000,49000,'paid','none',1,49000,0,'2026-04-27 18:04:31','2026-04-27 17:04:30','2026-04-27 17:04:31',NULL,NULL,NULL,NULL,'2026-04-27 18:04:43','工厂超时未确认，系统自动取消订单',0,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(2,'ORD-1777280938096',4,1,3,'completed',1,'2026-04-27','2026-04-27',NULL,NULL,NULL,13100,8789400,8802500,'paid','none',2,8802500,0,'2026-04-27 18:08:58','2026-04-27 17:08:58','2026-04-27 17:08:58','2026-04-27 17:15:21','2026-04-27 17:16:30','2026-04-27 17:17:09','2026-04-30 18:18:51',NULL,NULL,1,'由前端方案提交生成',1,'2026-04-30 18:18:51'),(3,'ORD-1777282099174',7,1,3,'completed',1,'2026-04-27','2026-04-27',NULL,NULL,NULL,26300,8877100,8903400,'paid','none',3,8903400,0,'2026-04-27 18:28:19','2026-04-27 17:28:19','2026-04-27 17:28:19','2026-04-27 17:28:38','2026-04-27 17:28:50','2026-04-27 17:31:03','2026-04-30 18:18:51',NULL,NULL,1,'由前端方案提交生成',1,'2026-04-30 18:18:51'),(4,'ORD-1777339437991',8,1,3,'completed',150,'2026-04-28','2026-09-24',NULL,NULL,NULL,1170000,50000,1220000,'paid','none',4,1220000,0,'2026-04-28 10:23:58','2026-04-28 09:23:57','2026-04-28 09:23:58','2026-04-28 09:25:20','2026-04-28 16:48:47','2026-04-28 16:48:51','2026-05-01 22:25:15',NULL,NULL,1,'由前端方案提交生成',1,'2026-05-01 22:25:15'),(5,'ORD-1777366166645',9,1,3,'cancelled_manual',19,'2026-04-28','2026-05-16','这里','舞台剧','没有',186200,70000,256200,'paid','none',5,256200,0,'2026-04-28 17:49:27','2026-04-28 16:49:26','2026-04-28 16:49:27',NULL,NULL,NULL,NULL,'2026-04-28 16:51:09','不合适',0,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(6,'ORD-1777366656925',10,1,3,'completed',12,'2026-04-28','2026-05-09','本次地址','哈哈哈','没有',117600,70000,187600,'paid','none',6,187600,0,'2026-04-28 17:57:37','2026-04-28 16:57:36','2026-04-28 16:57:37','2026-04-28 17:00:11','2026-04-28 17:01:01','2026-04-28 17:01:14','2026-04-28 21:00:16',NULL,NULL,1,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(7,'ORD-1777446080038',11,1,3,'cancelled_timeout',13,'2026-04-29','2026-05-11','这里','111','🌧️',91000,70000,161000,'paid','none',7,161000,0,'2026-04-29 16:01:20','2026-04-29 15:01:20','2026-04-29 15:01:20',NULL,NULL,NULL,NULL,'2026-04-29 16:01:30','工厂超时未确认，系统自动取消订单',0,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(8,'ORD-1777451974009',12,1,3,'cancelled_manual',19,'2026-04-29','2026-05-17','这里','1','',133000,70000,203000,'paid','none',8,203000,0,'2026-04-29 17:39:34','2026-04-29 16:39:34','2026-04-29 16:39:34',NULL,NULL,NULL,NULL,'2026-04-29 16:40:07','不同意',0,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(9,'ORD-1777452059507',13,1,3,'completed',12,'2026-04-29','2026-05-10','宇','1','',84000,70000,154000,'paid','none',9,154000,0,'2026-04-29 17:41:00','2026-04-29 16:40:59','2026-04-29 16:41:00','2026-04-29 16:41:22','2026-04-29 16:41:32','2026-04-29 16:42:09','2026-04-29 16:43:28',NULL,NULL,1,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(10,'ORD-1777452500301',15,1,3,'completed',1,'2026-04-29','2026-04-29','吧','吧','',5000,50000,55000,'paid','none',10,55000,0,'2026-04-29 17:48:20','2026-04-29 16:48:20','2026-04-29 16:48:20','2026-04-29 16:49:54','2026-04-29 16:50:00','2026-04-29 16:50:02','2026-04-29 16:51:30',NULL,NULL,1,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(11,'ORD-1777452992973',14,1,3,'completed',5,'2026-04-29','2026-05-03','这里','🌧️','',35000,70000,105000,'paid','none',11,105000,0,'2026-04-29 17:56:33','2026-04-29 16:56:32','2026-04-29 16:56:33','2026-04-29 16:56:42','2026-04-29 16:56:44','2026-04-26 16:56:46','2026-04-29 16:59:11',NULL,NULL,1,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(12,'ORD-1777465426830',16,1,3,'cancelled_manual',5,'2026-04-29','2026-05-03','地址','场景','备注',75000,100000,175000,'paid','none',12,175000,0,'2026-04-29 21:23:47','2026-04-29 20:23:46','2026-04-29 20:23:47',NULL,NULL,NULL,NULL,'2026-04-29 20:26:00','不',0,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(13,'ORD-1777465602381',17,1,3,'completed',50,'2026-04-29','2026-06-17','1','1','1',600000,70000,670000,'paid','none',13,670000,0,'2026-04-29 21:26:42','2026-04-29 20:26:42','2026-04-29 20:26:42','2026-04-29 20:26:58','2026-04-29 20:27:12','2026-04-29 20:28:30','2026-04-29 20:30:23',NULL,NULL,1,'由前端方案提交生成',1,'2026-04-30 18:18:47'),(14,'ORD-1778144536627',18,1,3,'completed',10,'2026-05-01','2026-05-10','地址','舞会','',80000,50000,130000,'paid','none',14,130000,0,'2026-05-07 18:02:17','2026-05-07 17:02:16','2026-05-07 17:02:17','2026-05-07 17:03:14','2026-05-07 17:04:27','2026-05-07 18:18:14','2026-05-10 22:01:08',NULL,NULL,1,'由前端方案提交生成',1,'2026-05-10 22:01:08'),(15,'ORD-1778149154636',19,1,3,'completed',4,'2026-05-07','2026-05-10','心里','舞台','',32000,50000,82000,'paid','settled',15,82000,50000,'2026-05-07 19:19:15','2026-05-07 18:19:14','2026-05-07 18:19:15','2026-05-07 18:20:06','2026-05-07 23:10:36','2026-05-08 18:16:46','2026-05-12 17:42:21',NULL,NULL,1,'由前端方案提交生成',1,'2026-05-12 17:42:20'),(16,'ORD-1778244378143',20,1,3,'completed',6,'2026-05-08','2026-05-13','你好','场景','备注',48000,50000,98000,'paid','settled',16,98000,50000,'2026-05-08 21:46:18','2026-05-08 20:46:18','2026-05-08 20:46:18','2026-05-08 20:47:21','2026-05-08 20:48:34','2026-05-08 20:48:48','2026-05-12 17:42:21',NULL,NULL,1,'由前端方案提交生成',1,'2026-05-12 17:42:20');
/*!40000 ALTER TABLE `rental_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental_order_item`
--

DROP TABLE IF EXISTS `rental_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rental_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单道具明细主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '关联订单ID',
  `prop_id` bigint(20) NOT NULL COMMENT '关联道具ID',
  `prop_name_snapshot` varchar(128) NOT NULL COMMENT '道具名称快照',
  `image_url_snapshot` varchar(255) DEFAULT NULL COMMENT '道具图片快照',
  `daily_rent_price_fen_snapshot` int(11) NOT NULL DEFAULT '0' COMMENT '下单时日租金快照，单位分',
  `deposit_amount_fen_snapshot` int(11) NOT NULL DEFAULT '0' COMMENT '下单时押金快照，单位分',
  `outbound_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '出库状态：pending待出库 scanned已出库',
  `return_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '归还状态：pending待归还 scanned已归还',
  `outbound_scanned_at` datetime DEFAULT NULL COMMENT '出库扫码时间',
  `return_scanned_at` datetime DEFAULT NULL COMMENT '归还扫码时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_prop` (`order_id`,`prop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COMMENT='订单道具明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental_order_item`
--

LOCK TABLES `rental_order_item` WRITE;
/*!40000 ALTER TABLE `rental_order_item` DISABLE KEYS */;
INSERT INTO `rental_order_item` VALUES (1,1,118,'主持台 I02','/images/stage-prop-real.jpg',5000,15000,'pending','pending',NULL,NULL),(2,1,119,'舞台效果机 J01','/images/stage-prop-real.jpg',7000,22000,'pending','pending',NULL,NULL),(3,2,123,'新上架','/images/stage-prop-real.jpg',13100,8789400,'scanned','scanned','2026-04-27 17:16:30','2026-04-27 17:17:09'),(4,3,121,'0424','/images/stage-prop-real.jpg',13100,87500,'scanned','scanned','2026-04-27 17:28:40','2026-04-27 17:28:54'),(5,3,122,'吧','/images/stage-prop-real.jpg',100,200,'scanned','scanned','2026-04-27 17:28:49','2026-04-27 17:31:03'),(6,3,123,'新上架','/images/stage-prop-real.jpg',13100,8789400,'scanned','scanned','2026-04-27 17:28:50','2026-04-27 17:31:03'),(7,4,127,'🐮','wxfile://tmp_e2219a15c4f496a6ae756a5e967b1b6c.jpg',7800,50000,'scanned','scanned','2026-04-28 16:48:47','2026-04-28 16:48:51'),(8,5,127,'🐮','wxfile://tmp_e2219a15c4f496a6ae756a5e967b1b6c.jpg',7800,50000,'pending','pending',NULL,NULL),(9,5,128,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',2000,20000,'pending','pending',NULL,NULL),(10,6,127,'🐮','wxfile://tmp_e2219a15c4f496a6ae756a5e967b1b6c.jpg',7800,50000,'scanned','scanned','2026-04-28 17:00:12','2026-04-28 17:01:08'),(11,6,128,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',2000,20000,'scanned','scanned','2026-04-28 17:01:01','2026-04-28 17:01:14'),(12,7,128,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',2000,20000,'pending','pending',NULL,NULL),(13,7,129,'🐯','/images/stage-prop-real.jpg',5000,50000,'pending','pending',NULL,NULL),(14,8,128,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',2000,20000,'pending','pending',NULL,NULL),(15,8,129,'🐯','/images/stage-prop-real.jpg',5000,50000,'pending','pending',NULL,NULL),(16,9,128,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',2000,20000,'scanned','scanned','2026-04-29 16:41:28','2026-04-29 16:42:04'),(17,9,129,'🐯','/images/stage-prop-real.jpg',5000,50000,'scanned','scanned','2026-04-29 16:41:32','2026-04-29 16:42:09'),(18,10,129,'🐯','/images/stage-prop-real.jpg',5000,50000,'scanned','scanned','2026-04-29 16:50:00','2026-04-29 16:50:02'),(19,11,128,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',2000,20000,'scanned','scanned','2026-04-29 16:56:44','2026-04-29 16:56:46'),(20,11,129,'🐯','/images/stage-prop-real.jpg',5000,50000,'scanned','scanned','2026-04-29 16:56:44','2026-04-29 16:56:46'),(21,12,129,'🐯','/images/stage-prop-real.jpg',5000,50000,'pending','pending',NULL,NULL),(22,12,131,'🥵','wxfile://tmp_5cbee36ea9e9eafdca308f864286aba3.jpg',10000,50000,'pending','pending',NULL,NULL),(23,13,128,'🐔','wxfile://tmp_b3eb0cef59088c7b157923f6b3a28715.jpg',2000,20000,'scanned','scanned','2026-04-29 20:27:12','2026-04-29 20:27:32'),(24,13,131,'🥵','wxfile://tmp_5cbee36ea9e9eafdca308f864286aba3.jpg',10000,50000,'scanned','scanned','2026-04-29 20:27:12','2026-04-29 20:28:30'),(25,14,133,'古风屏风','wxfile://tmp_f90f6413970b32206eda902a8f5a26ec.jpg',8000,50000,'scanned','scanned','2026-05-07 17:04:27','2026-05-07 18:18:14'),(26,15,133,'古风屏风','wxfile://tmp_f90f6413970b32206eda902a8f5a26ec.jpg',8000,50000,'scanned','scanned','2026-05-07 23:10:36','2026-05-08 18:16:46'),(27,16,133,'古风屏风','wxfile://tmp_f90f6413970b32206eda902a8f5a26ec.jpg',8000,50000,'scanned','scanned','2026-05-08 20:48:34','2026-05-08 20:48:48');
/*!40000 ALTER TABLE `rental_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_settlement_audit`
--

DROP TABLE IF EXISTS `supplier_settlement_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `supplier_settlement_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工厂入驻审核主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '申请入驻的用户ID',
  `applicant_user_id` bigint(20) DEFAULT NULL COMMENT '申请用户ID',
  `invite_code_id` bigint(20) DEFAULT NULL COMMENT '邀请码ID',
  `company_name` varchar(128) NOT NULL COMMENT '工厂/公司名称',
  `factory_name` varchar(128) DEFAULT NULL COMMENT '工厂名称',
  `unified_social_credit_code` varchar(32) DEFAULT NULL COMMENT '统一社会信用代码',
  `business_license_url` varchar(255) DEFAULT NULL COMMENT '营业执照图片地址',
  `contact_name` varchar(64) NOT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) NOT NULL COMMENT '联系人电话',
  `factory_address` varchar(255) DEFAULT NULL COMMENT '工厂地址',
  `main_business` varchar(255) DEFAULT NULL COMMENT '主营道具类型',
  `remark` varchar(255) DEFAULT NULL COMMENT '申请备注',
  `license_no` varchar(64) DEFAULT NULL COMMENT '营业执照编号',
  `audit_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '审核状态：pending待审核 approved通过 rejected驳回',
  `audit_remark` varchar(255) DEFAULT NULL COMMENT '审核备注',
  `reject_reason` varchar(255) DEFAULT NULL COMMENT '驳回原因',
  `reviewer_id` bigint(20) DEFAULT NULL COMMENT '审核人用户ID',
  `reviewed_by_admin_user_id` bigint(20) DEFAULT NULL COMMENT '审核管理员ID',
  `submitted_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `reviewed_at` datetime DEFAULT NULL COMMENT '审核时间',
  `created_factory_id` bigint(20) DEFAULT NULL COMMENT '创建后的工厂主体ID',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='工厂入驻审核表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_settlement_audit`
--

LOCK TABLES `supplier_settlement_audit` WRITE;
/*!40000 ALTER TABLE `supplier_settlement_audit` DISABLE KEYS */;
INSERT INTO `supplier_settlement_audit` VALUES (1,4,4,NULL,'星幕舞美工厂','星幕舞美工厂',NULL,NULL,'李师傅','13800000004',NULL,NULL,NULL,NULL,'rejected',NULL,NULL,3,3,'2026-04-19 15:34:57','2026-04-27 16:20:38',NULL,'2026-04-27 16:20:38'),(2,5,5,NULL,'云启道具工作室','云启道具工作室',NULL,NULL,'陈经理','13800000005',NULL,NULL,NULL,NULL,'rejected',NULL,NULL,3,3,'2026-04-20 15:34:57','2026-04-27 16:20:37',NULL,'2026-04-27 16:20:37');
/*!40000 ALTER TABLE `supplier_settlement_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `nickname` varchar(64) NOT NULL COMMENT '用户昵称',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '用户头像地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `realname_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已实名认证：0否 1是',
  `student_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已学生认证：0否 1是',
  `credit_score` int(11) NOT NULL DEFAULT '80' COMMENT '用户信用值，工厂查看订单时展示',
  `user_status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '用户状态：active正常 disabled停用 suspended冻结',
  `register_status` varchar(32) NOT NULL DEFAULT 'active' COMMENT '注册状态：new/active/factory_pending/factory_rejected/disabled',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'租赁端演示账号','/images/avatar.png','13800000001',1,1,98,'active','active','2026-04-21 15:34:57','2026-04-28 16:40:45'),(2,'工厂端演示账号','/images/avatar.png','13800000002',1,1,98,'active','active','2026-04-21 15:34:57','2026-04-28 16:40:45'),(3,'管理员演示账号','/images/avatar.png','13800000003',1,1,98,'active','active','2026-04-21 15:34:57','2026-04-28 16:40:45'),(4,'星幕舞美工厂','/images/avatar.png','13800000004',1,0,90,'active','factory_rejected','2026-04-21 15:34:57','2026-04-28 16:40:45'),(5,'云启道具工作室','/images/avatar.png','13800000005',1,0,90,'active','factory_rejected','2026-04-21 15:34:57','2026-04-28 16:40:45'),(6,'新用户演示账号','/images/avatar.png','13800000006',0,0,80,'active','new','2026-04-21 15:45:04','2026-04-29 21:10:16'),(7,'微信用户2ljRlq','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 15:45:06','2026-04-21 15:45:06'),(8,'微信用户3NUZF9','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 15:45:06','2026-04-21 15:45:06'),(9,'微信用户1hPrFn','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 15:45:15','2026-04-21 15:45:15'),(10,'微信用户1y3yGn','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 15:45:49','2026-04-21 15:45:49'),(11,'微信用户260RkB','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:21:37','2026-04-21 17:21:37'),(12,'微信用户15sr0q','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:23:20','2026-04-21 17:23:20'),(13,'微信用户0payF8','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:29:15','2026-04-21 17:29:15'),(14,'微信用户0iy8ld','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:29:52','2026-04-21 17:29:52'),(15,'微信用户3AKem4','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:29:54','2026-04-21 17:29:54'),(16,'微信用户2jDplF','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:30:23','2026-04-21 17:30:23'),(17,'微信用户4GPemI','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:31:15','2026-04-21 17:31:15'),(18,'微信用户3JD8lh','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:31:18','2026-04-21 17:31:18'),(19,'微信用户3puEGI','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:31:20','2026-04-21 17:31:20'),(20,'微信用户2po6G3','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 17:31:21','2026-04-21 17:31:21'),(21,'微信用户18bMFY','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 21:19:01','2026-04-21 21:19:01'),(22,'微信用户3XwHH6','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 21:19:11','2026-04-21 21:19:11'),(23,'微信用户2oADlb','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 21:19:12','2026-04-21 21:19:12'),(24,'微信用户0eYdmv','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 21:56:42','2026-04-21 21:56:42'),(25,'微信用户0fvOFT','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 21:57:09','2026-04-21 21:57:09'),(26,'微信用户1TYI0O','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:10:48','2026-04-21 22:10:48'),(27,'微信用户00iyFS','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:10:51','2026-04-21 22:10:51'),(28,'微信用户18o6Gg','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:10:52','2026-04-21 22:10:52'),(29,'微信用户12TvmT','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:10:56','2026-04-21 22:10:56'),(30,'微信用户1yZI0Z','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:10:58','2026-04-21 22:10:58'),(31,'微信用户1KTa0P','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:11:02','2026-04-21 22:11:02'),(32,'微信用户3WWr0U','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:11:04','2026-04-21 22:11:04'),(33,'微信用户3D601h','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:12:02','2026-04-21 22:12:02'),(34,'微信用户3yGcH2','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:12:32','2026-04-21 22:12:32'),(35,'微信用户3ANplp','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:12:42','2026-04-21 22:12:42'),(36,'微信用户0dUXlD','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:12:51','2026-04-21 22:12:51'),(37,'微信用户19WXlJ','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:13:22','2026-04-21 22:13:22'),(38,'微信用户0bHVG4','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:13:30','2026-04-21 22:13:30'),(39,'微信用户1UN8ln','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:13:35','2026-04-21 22:13:35'),(40,'微信用户2p3b0C','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:13:35','2026-04-21 22:13:35'),(41,'微信用户2WN8lZ','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:13:36','2026-04-21 22:13:36'),(42,'微信用户0dwPF-','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:13:48','2026-04-21 22:13:48'),(43,'微信用户1UR8ls','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:14:39','2026-04-21 22:14:39'),(44,'微信用户1JzPFc','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:14:44','2026-04-21 22:14:44'),(45,'微信用户2jMVG4','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:14:51','2026-04-21 22:14:51'),(46,'微信用户2bMnGj','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:16:25','2026-04-21 22:16:25'),(47,'微信用户2OsGGA','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:43:18','2026-04-21 22:43:18'),(48,'微信用户3lCalK','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:43:20','2026-04-21 22:43:20'),(49,'微信用户26kRFU','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:43:24','2026-04-21 22:43:24'),(50,'微信用户05VVZd','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:45:00','2026-04-21 22:45:00'),(51,'微信用户07ECkc','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:45:24','2026-04-21 22:45:24'),(52,'微信用户26sRF1','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:45:31','2026-04-21 22:45:31'),(53,'微信用户2cpAFv','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:45:33','2026-04-21 22:45:33'),(54,'微信用户4hITkG','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:45:42','2026-04-21 22:45:42'),(55,'微信用户3mtRFY','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:45:51','2026-04-21 22:45:51'),(56,'微信用户36SIlv','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:45:54','2026-04-21 22:45:54'),(57,'微信用户3vRalH','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:47:20','2026-04-21 22:47:20'),(58,'微信用户1PRal9','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:47:26','2026-04-21 22:47:26'),(59,'微信用户245hmc','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:47:43','2026-04-21 22:47:43'),(60,'微信用户3DxAFK','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:47:47','2026-04-21 22:47:47'),(61,'微信用户0409GC','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:53:42','2026-04-21 22:53:42'),(62,'微信用户3lE21y','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:53:45','2026-04-21 22:53:45'),(63,'微信用户0Y9Dku','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:53:50','2026-04-21 22:53:50'),(64,'微信用户3WUAFR','/images/avatar.png',NULL,0,0,80,'active','active','2026-04-21 22:53:57','2026-04-21 22:53:57'),(65,'mbro','wxfile://tmp_e2504e11b6b8181036a3367f8144d5e7.jpg','18006445355',1,0,80,'active','active','2026-05-02 23:46:14','2026-05-03 18:49:52');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户角色绑定主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '关联用户ID',
  `role_code` varchar(20) NOT NULL COMMENT '角色编码：demander需求方 supplier工厂 admin管理员',
  `enabled` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否启用：0否 1是',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色绑定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1,'demander',1,'2026-04-21 15:34:57'),(2,2,'supplier',0,'2026-04-21 15:34:57'),(3,3,'admin',1,'2026-04-21 15:34:57'),(4,4,'supplier',1,'2026-04-21 15:34:57'),(5,5,'supplier',1,'2026-04-21 15:34:57'),(6,6,'demander',0,'2026-04-21 15:45:04'),(7,7,'demander',1,'2026-04-21 15:45:06'),(8,8,'demander',1,'2026-04-21 15:45:06'),(9,9,'demander',1,'2026-04-21 15:45:15'),(10,10,'demander',1,'2026-04-21 15:45:49'),(11,11,'demander',1,'2026-04-21 17:21:37'),(12,12,'demander',1,'2026-04-21 17:23:20'),(13,13,'demander',1,'2026-04-21 17:29:15'),(14,14,'demander',1,'2026-04-21 17:29:52'),(15,15,'demander',1,'2026-04-21 17:29:54'),(16,16,'demander',1,'2026-04-21 17:30:23'),(17,17,'demander',1,'2026-04-21 17:31:15'),(18,18,'demander',1,'2026-04-21 17:31:18'),(19,19,'demander',1,'2026-04-21 17:31:20'),(20,20,'demander',1,'2026-04-21 17:31:21'),(21,21,'demander',1,'2026-04-21 21:19:02'),(22,22,'demander',1,'2026-04-21 21:19:11'),(23,23,'demander',1,'2026-04-21 21:19:12'),(24,24,'demander',1,'2026-04-21 21:56:42'),(25,25,'demander',1,'2026-04-21 21:57:09'),(26,26,'demander',1,'2026-04-21 22:10:48'),(27,27,'demander',1,'2026-04-21 22:10:51'),(28,28,'demander',1,'2026-04-21 22:10:52'),(29,29,'demander',1,'2026-04-21 22:10:56'),(30,30,'demander',1,'2026-04-21 22:10:58'),(31,31,'demander',1,'2026-04-21 22:11:02'),(32,32,'demander',1,'2026-04-21 22:11:04'),(33,33,'demander',1,'2026-04-21 22:12:02'),(34,34,'demander',1,'2026-04-21 22:12:32'),(35,35,'demander',1,'2026-04-21 22:12:42'),(36,36,'demander',1,'2026-04-21 22:12:51'),(37,37,'demander',1,'2026-04-21 22:13:22'),(38,38,'demander',1,'2026-04-21 22:13:30'),(39,39,'demander',1,'2026-04-21 22:13:35'),(40,40,'demander',1,'2026-04-21 22:13:35'),(41,41,'demander',1,'2026-04-21 22:13:36'),(42,42,'demander',1,'2026-04-21 22:13:48'),(43,43,'demander',1,'2026-04-21 22:14:39'),(44,44,'demander',1,'2026-04-21 22:14:44'),(45,45,'demander',1,'2026-04-21 22:14:51'),(46,46,'demander',1,'2026-04-21 22:16:25'),(47,3,'supplier',1,'2026-04-21 22:42:23'),(51,47,'demander',1,'2026-04-21 22:43:18'),(52,48,'demander',1,'2026-04-21 22:43:20'),(53,49,'demander',1,'2026-04-21 22:43:24'),(54,50,'demander',1,'2026-04-21 22:45:00'),(55,51,'demander',1,'2026-04-21 22:45:24'),(56,52,'demander',1,'2026-04-21 22:45:31'),(57,53,'demander',1,'2026-04-21 22:45:33'),(58,54,'demander',1,'2026-04-21 22:45:42'),(59,55,'demander',1,'2026-04-21 22:45:51'),(60,56,'demander',1,'2026-04-21 22:45:54'),(61,57,'demander',1,'2026-04-21 22:47:20'),(62,58,'demander',1,'2026-04-21 22:47:26'),(63,59,'demander',1,'2026-04-21 22:47:43'),(64,60,'demander',1,'2026-04-21 22:47:47'),(65,61,'demander',1,'2026-04-21 22:53:42'),(66,62,'demander',1,'2026-04-21 22:53:45'),(67,63,'demander',1,'2026-04-21 22:53:50'),(68,64,'demander',1,'2026-04-21 22:53:57'),(73,6,'supplier',0,'2026-04-27 17:45:41'),(74,65,'demander',1,'2026-05-03 18:49:52');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_wechat`
--

DROP TABLE IF EXISTS `sys_user_wechat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_wechat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '微信绑定主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '关联用户ID',
  `openid` varchar(64) NOT NULL COMMENT '微信OpenID',
  `unionid` varchar(64) DEFAULT NULL COMMENT '微信UnionID',
  `session_key` varchar(128) DEFAULT NULL COMMENT '微信会话密钥，可选短期存储',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COMMENT='用户微信绑定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_wechat`
--

LOCK TABLES `sys_user_wechat` WRITE;
/*!40000 ALTER TABLE `sys_user_wechat` DISABLE KEYS */;
INSERT INTO `sys_user_wechat` VALUES (1,6,'mock-openid-0f327Lkl23cLzh4hUfml2PmpFX127LkG',NULL,'demo-session-new-user','2026-04-21 15:45:04','2026-04-25 15:48:57'),(2,7,'mock-openid-0e3ljRll24ZEyh4cyDnl20Cbls2ljRlq',NULL,'mock-session-key','2026-04-21 15:45:06','2026-04-21 15:45:06'),(3,8,'mock-openid-0e3NUZFa1FbhzL0jEsIa1KeN7E3NUZF9',NULL,'mock-session-key','2026-04-21 15:45:06','2026-04-21 15:45:06'),(4,9,'mock-openid-0b3hPrFa1a1VzL0qIiGa1oeufU1hPrFn',NULL,'mock-session-key','2026-04-21 15:45:15','2026-04-21 15:45:15'),(5,10,'mock-openid-0c3y3yGa1XROyL0wBtHa134Tuo1y3yGn',NULL,'mock-session-key','2026-04-21 15:45:49','2026-04-21 15:45:49'),(6,11,'mock-openid-0d360Rkl27MPzh4AOoml2rLfks260RkB',NULL,'mock-session-key','2026-04-21 17:21:37','2026-04-21 17:21:37'),(7,12,'mock-openid-0b35sr0w3CyfU63k1T2w3yC3pV15sr0q',NULL,'mock-session-key','2026-04-21 17:23:20','2026-04-21 17:23:20'),(8,13,'mock-openid-0c3payFa1V1UzL0LbdJa1oj1I40payF8',NULL,'mock-session-key','2026-04-21 17:29:15','2026-04-21 17:29:15'),(9,14,'mock-openid-0c3iy8ll2OSyzh4U0tll2fLvXv0iy8ld',NULL,'mock-session-key','2026-04-21 17:29:52','2026-04-21 17:29:52'),(10,15,'mock-openid-0f3AKeml2oHsyh4o40ol2p5Fu93AKem4',NULL,'mock-session-key','2026-04-21 17:29:54','2026-04-21 17:29:54'),(11,16,'mock-openid-0d3jDpll2Elizh41LCnl2EDqgA2jDplF',NULL,'mock-session-key','2026-04-21 17:30:23','2026-04-21 17:30:23'),(12,17,'mock-openid-0a3GPeml2r8tyh4b58ol2zw5ft4GPemI',NULL,'mock-session-key','2026-04-21 17:31:15','2026-04-21 17:31:15'),(13,18,'mock-openid-0a3JD8ll2zjzzh4duLll2gExCQ3JD8lh',NULL,'mock-session-key','2026-04-21 17:31:18','2026-04-21 17:31:18'),(14,19,'mock-openid-0c3puEGa1ZgOyL0oYTGa1sFrZd3puEGI',NULL,'mock-session-key','2026-04-21 17:31:20','2026-04-21 17:31:20'),(15,20,'mock-openid-0b3po6Ga1mnmzL084VFa14xObr2po6G3',NULL,'mock-session-key','2026-04-21 17:31:21','2026-04-21 17:31:21'),(16,21,'mock-openid-0b38bMFa1DXFzL0pxyGa1yIPxx18bMFY',NULL,'mock-session-key','2026-04-21 21:19:02','2026-04-21 21:19:02'),(17,22,'mock-openid-0d3XwHHa1Fn9CL0Cd8Ha1r0AqG3XwHH6',NULL,'mock-session-key','2026-04-21 21:19:11','2026-04-21 21:19:11'),(18,23,'mock-openid-0d3oADll2sJ3zh47Jcll2WPlfZ2oADlb',NULL,'mock-session-key','2026-04-21 21:19:12','2026-04-21 21:19:12'),(19,24,'mock-openid-0a3eYdml2o1oyh4d9sll2Ca7680eYdmv',NULL,'mock-session-key','2026-04-21 21:56:42','2026-04-21 21:56:42'),(20,25,'mock-openid-0f3fvOFa1NlyzL0s27Ia1rNdDB0fvOFT',NULL,'mock-session-key','2026-04-21 21:57:09','2026-04-21 21:57:09'),(21,26,'mock-openid-0b3TYI0w3yOUT63ubW2w3HOxMH1TYI0O',NULL,'mock-session-key','2026-04-21 22:10:48','2026-04-21 22:10:48'),(22,27,'mock-openid-0a30iyFa1KjQzL0ARoIa1geHdh00iyFS',NULL,'mock-session-key','2026-04-21 22:10:51','2026-04-21 22:10:51'),(23,28,'mock-openid-0d38o6Ga17dizL0FiaJa1qp7i618o6Gg',NULL,'mock-session-key','2026-04-21 22:10:52','2026-04-21 22:10:52'),(24,29,'mock-openid-0e32Tvml2mU7yh4I5qml2Cfv2t12TvmT',NULL,'mock-session-key','2026-04-21 22:10:56','2026-04-21 22:10:56'),(25,30,'mock-openid-0e3yZI0w38OUT63wUB0w3kAGxW1yZI0Z',NULL,'mock-session-key','2026-04-21 22:10:58','2026-04-21 22:10:58'),(26,31,'mock-openid-0f3KTa0w3KTsU63NPe0w3IWJoV1KTa0P',NULL,'mock-session-key','2026-04-21 22:11:02','2026-04-21 22:11:02'),(27,32,'mock-openid-0e3WWr0w3AQbU63gAA2w30FkTS3WWr0U',NULL,'mock-session-key','2026-04-21 22:11:04','2026-04-21 22:11:04'),(28,33,'mock-openid-0b3D601w3rYDT63hDt0w3tRv5M3D601h',NULL,'mock-session-key','2026-04-21 22:12:02','2026-04-21 22:12:02'),(29,34,'mock-openid-0c3yGcHa13ecyL0L0jIa1qu9m03yGcH2',NULL,'mock-session-key','2026-04-21 22:12:32','2026-04-21 22:12:32'),(30,35,'mock-openid-0b3ANpll29iezh40QYol2MZ5kx3ANplp',NULL,'mock-session-key','2026-04-21 22:12:42','2026-04-21 22:12:42'),(31,36,'mock-openid-0e3dUXll25cGyh49mznl2ndu6W0dUXlD',NULL,'mock-session-key','2026-04-21 22:12:51','2026-04-21 22:12:51'),(32,37,'mock-openid-0d39WXll2c5Gyh4Uezol2TVoBG19WXlJ',NULL,'mock-session-key','2026-04-21 22:13:22','2026-04-21 22:13:22'),(33,38,'mock-openid-0c3bHVGa109tyL07jiJa1LdQbp0bHVG4',NULL,'mock-session-key','2026-04-21 22:13:30','2026-04-21 22:13:30'),(34,39,'mock-openid-0b3UN8ll2Tevzh4Fsfll2Wu7i61UN8ln',NULL,'mock-session-key','2026-04-21 22:13:35','2026-04-21 22:13:35'),(35,40,'mock-openid-0e3p3b0w3oZsU63V3l1w3LqKdI2p3b0C',NULL,'mock-session-key','2026-04-21 22:13:35','2026-04-21 22:13:35'),(36,41,'mock-openid-0b3WN8ll2rfvzh4ye6nl2su1eI2WN8lZ',NULL,'mock-session-key','2026-04-21 22:13:36','2026-04-21 22:13:36'),(37,42,'mock-openid-0b3dwPFa1nmzzL0pzbGa1ZAc4g0dwPF-',NULL,'mock-session-key','2026-04-21 22:13:48','2026-04-21 22:13:48'),(38,43,'mock-openid-0e3UR8ll2o3vzh4piwnl2gRjqw1UR8ls',NULL,'mock-session-key','2026-04-21 22:14:39','2026-04-21 22:14:39'),(39,44,'mock-openid-0c3JzPFa1KazzL0QKAJa1L02jM1JzPFc',NULL,'mock-session-key','2026-04-21 22:14:44','2026-04-21 22:14:44'),(40,45,'mock-openid-0f3jMVGa10XsyL0fBKFa1jQukJ2jMVG4',NULL,'mock-session-key','2026-04-21 22:14:51','2026-04-21 22:14:51'),(41,46,'mock-openid-0e3bMnGa1AU0zL0r6uGa1JBEcI2bMnGj',NULL,'mock-session-key','2026-04-21 22:16:25','2026-04-21 22:16:25'),(42,47,'mock-openid-0e3OsGGa1g9LyL00fsGa1KelyT2OsGGA',NULL,'mock-session-key','2026-04-21 22:43:18','2026-04-21 22:43:18'),(43,48,'mock-openid-0a3lCall2Kbwzh4vHMnl2rvW1v3lCalK',NULL,'mock-session-key','2026-04-21 22:43:20','2026-04-21 22:43:20'),(44,49,'mock-openid-0e36kRFa1wiAzL0mzGFa1qEXdj26kRFU',NULL,'mock-session-key','2026-04-21 22:43:24','2026-04-21 22:43:24'),(45,50,'mock-openid-0a35VVZv34DvU63rUN2w3MLIPu05VVZd',NULL,'mock-session-key','2026-04-21 22:45:00','2026-04-21 22:45:00'),(46,51,'mock-openid-0b37ECkl2HVOzh4Baoll2UZz4g07ECkc',NULL,'mock-session-key','2026-04-21 22:45:24','2026-04-21 22:45:24'),(47,52,'mock-openid-0e36sRFa1zWkzL0PqqFa1Xwp8z26sRF1',NULL,'mock-session-key','2026-04-21 22:45:31','2026-04-21 22:45:31'),(48,53,'mock-openid-0e3cpAFa1XZBzL0PjYFa1BFDhR2cpAFv',NULL,'mock-session-key','2026-04-21 22:45:33','2026-04-21 22:45:33'),(49,54,'mock-openid-0e3hITkl29Qxzh4GDall24cbjd4hITkG',NULL,'mock-session-key','2026-04-21 22:45:42','2026-04-21 22:45:42'),(50,55,'mock-openid-0d3mtRFa1fTkzL0u0bGa1vxAyY3mtRFY',NULL,'mock-session-key','2026-04-21 22:45:51','2026-04-21 22:45:51'),(51,56,'mock-openid-0f36SIll2RGIyh4qMYnl2V3pkp36SIlv',NULL,'mock-session-key','2026-04-21 22:45:54','2026-04-21 22:45:54'),(52,57,'mock-openid-0d3vRall21Zgzh4gxDml22zJ0U3vRalH',NULL,'mock-session-key','2026-04-21 22:47:20','2026-04-21 22:47:20'),(53,58,'mock-openid-0c3PRall2CYgzh43Synl2A4KL11PRal9',NULL,'mock-session-key','2026-04-21 22:47:26','2026-04-21 22:47:26'),(54,59,'mock-openid-0b345hml2NBayh4U0Bml2XeB3P245hmc',NULL,'mock-session-key','2026-04-21 22:47:43','2026-04-21 22:47:43'),(55,60,'mock-openid-0c3DxAFa14YBzL0nJxFa1IwZL33DxAFK',NULL,'mock-session-key','2026-04-21 22:47:47','2026-04-21 22:47:47'),(56,61,'mock-openid-0c3409Ga1St3zL0D5pGa1Tr9VD0409GC',NULL,'mock-session-key','2026-04-21 22:53:42','2026-04-21 22:53:42'),(57,62,'mock-openid-0c3lE21w3Q1pT63OZu0w3ChbO33lE21y',NULL,'mock-session-key','2026-04-21 22:53:45','2026-04-21 22:53:45'),(58,63,'mock-openid-0d3Y9Dkl29wOzh4FbRnl2B7jY60Y9Dku',NULL,'mock-session-key','2026-04-21 22:53:50','2026-04-21 22:53:50'),(59,64,'mock-openid-0a3WUAFa1KzBzL0gUxJa1ZBb7E3WUAFR',NULL,'mock-session-key','2026-04-21 22:53:57','2026-04-21 22:53:57'),(60,1,'demo-openid-demander',NULL,'demo-session-demander','2026-04-25 15:48:57','2026-04-25 15:48:57'),(61,2,'demo-openid-supplier',NULL,'demo-session-supplier','2026-04-25 15:48:57','2026-04-25 15:48:57'),(62,3,'demo-openid-admin',NULL,'demo-session-admin','2026-04-25 15:48:57','2026-04-25 15:48:57'),(63,65,'oar423RNBxPQ0bSzRsiwI-S54je0',NULL,'lbH8RXk3unQl5XX0GjaWpA==','2026-05-02 23:46:14','2026-05-12 17:52:46');
/*!40000 ALTER TABLE `sys_user_wechat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_log`
--

DROP TABLE IF EXISTS `user_credit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_credit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '信用分流水ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID，关联 sys_user.id',
  `change_type` varchar(32) NOT NULL COMMENT '变动类型：init初始化、order订单、review评价、dispute纠纷、manual人工调整',
  `biz_type` varchar(32) DEFAULT NULL COMMENT '业务类型：order订单、review评价、dispute纠纷、system系统',
  `biz_id` bigint(20) DEFAULT NULL COMMENT '关联业务ID，例如订单ID、评价ID、纠纷ID',
  `before_score` int(11) NOT NULL COMMENT '变动前信用分',
  `delta_score` int(11) NOT NULL COMMENT '本次变动分值，正数加分，负数扣分',
  `after_score` int(11) NOT NULL COMMENT '变动后信用分',
  `reason` varchar(255) NOT NULL COMMENT '信用分变动原因',
  `operator_user_id` bigint(20) DEFAULT NULL COMMENT '操作人用户ID，系统自动变动为空',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_credit_log_user_time` (`user_id`,`created_at`),
  KEY `idx_credit_log_biz` (`biz_type`,`biz_id`),
  CONSTRAINT `fk_credit_log_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COMMENT='用户信用分变动流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_log`
--

LOCK TABLES `user_credit_log` WRITE;
/*!40000 ALTER TABLE `user_credit_log` DISABLE KEYS */;
INSERT INTO `user_credit_log` VALUES (1,1,'init','system',NULL,98,0,98,'系统初始化信用分',NULL,'2026-04-21 15:34:57'),(2,2,'init','system',NULL,98,0,98,'系统初始化信用分',NULL,'2026-04-21 15:34:57'),(3,3,'init','system',NULL,98,0,98,'系统初始化信用分',NULL,'2026-04-21 15:34:57'),(4,4,'init','system',NULL,90,0,90,'系统初始化信用分',NULL,'2026-04-21 15:34:57'),(5,5,'init','system',NULL,90,0,90,'系统初始化信用分',NULL,'2026-04-21 15:34:57'),(6,6,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 15:45:04'),(7,7,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 15:45:06'),(8,8,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 15:45:06'),(9,9,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 15:45:15'),(10,10,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 15:45:49'),(11,11,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:21:37'),(12,12,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:23:20'),(13,13,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:29:15'),(14,14,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:29:52'),(15,15,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:29:54'),(16,16,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:30:23'),(17,17,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:31:15'),(18,18,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:31:18'),(19,19,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:31:20'),(20,20,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 17:31:21'),(21,21,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 21:19:01'),(22,22,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 21:19:11'),(23,23,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 21:19:12'),(24,24,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 21:56:42'),(25,25,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 21:57:09'),(26,26,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:10:48'),(27,27,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:10:51'),(28,28,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:10:52'),(29,29,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:10:56'),(30,30,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:10:58'),(31,31,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:11:02'),(32,32,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:11:04'),(33,33,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:12:02'),(34,34,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:12:32'),(35,35,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:12:42'),(36,36,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:12:51'),(37,37,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:13:22'),(38,38,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:13:30'),(39,39,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:13:35'),(40,40,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:13:35'),(41,41,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:13:36'),(42,42,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:13:48'),(43,43,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:14:39'),(44,44,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:14:44'),(45,45,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:14:51'),(46,46,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:16:25'),(47,47,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:43:18'),(48,48,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:43:20'),(49,49,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:43:24'),(50,50,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:45:00'),(51,51,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:45:24'),(52,52,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:45:31'),(53,53,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:45:33'),(54,54,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:45:42'),(55,55,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:45:51'),(56,56,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:45:54'),(57,57,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:47:20'),(58,58,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:47:26'),(59,59,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:47:43'),(60,60,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:47:47'),(61,61,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:53:42'),(62,62,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:53:45'),(63,63,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:53:50'),(64,64,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-04-21 22:53:57'),(65,65,'init','system',NULL,80,0,80,'系统初始化信用分',NULL,'2026-05-02 23:46:14');
/*!40000 ALTER TABLE `user_credit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'muhou'
--

--
-- Dumping routines for database 'muhou'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-18 20:48:51
