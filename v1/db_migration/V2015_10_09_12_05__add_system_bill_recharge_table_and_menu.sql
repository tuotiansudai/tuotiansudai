BEGIN;

CREATE TABLE `system_recharge` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `time` datetime NOT NULL,
  `money` double NOT NULL,
  `success_time` datetime DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_SYSTEM_RECHARGE_USER_ID_REF_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/******添加“手动充值”菜单******/
INSERT INTO `menu` VALUES ('system_bill_recharge','Management','平台账户充值','/admin/fund/systemBillEdit.htm','recharge','1','2','','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);

/******添加“手动充值”权限******/
INSERT INTO `permission` VALUES ('system_bill_recharge','平台账户充值','平台账户充值') on duplicate key update `name`=values(`name`),description=values(description);

/******给“手动充值”菜单绑定“手动充值”权限******/
INSERT INTO `menu_permission` VALUES ('system_bill_recharge','system_bill_recharge') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

/****给(系统)管理员添加“手动充值”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','system_bill_recharge') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

COMMIT;