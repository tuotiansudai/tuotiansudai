BEGIN;
/******添加“充值”菜单******/
INSERT INTO `menu` VALUES ('recharge','Management','充值','','finance_mgr','1','4','','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);

/******添加“手动充值”菜单******/
INSERT INTO `menu` VALUES ('manual_recharge','Management','手动充值','/admin/fund/userBillEdit.htm','recharge','1','0','','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);

/******添加“批量充值”菜单******/
INSERT INTO `menu` VALUES ('batch_recharge','Management','批量充值','/admin/fund/rechargeBatchUpload.htm','recharge','1','1','','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);

/******添加“充值”权限******/
INSERT INTO `permission` VALUES ('recharge','充值','充值') on duplicate key update `name`=values(`name`),description=values(description);

/******添加“手动充值”权限******/
INSERT INTO `permission` VALUES ('manual_recharge','手动充值','手动充值') on duplicate key update `name`=values(`name`),description=values(description);

/******添加“批量充值”权限******/
INSERT INTO `permission` VALUES ('batch_recharge','批量充值','批量充值') on duplicate key update `name`=values(`name`),description=values(description);

/******添加“债权还款计划”权限******/
INSERT INTO `permission` VALUES ('creditor_repay_plan_list','债权还款计划','债权还款计划') on duplicate key update `name`=values(`name`),description=values(description);

/******给“债权还款计划”菜单绑定“债权还款计划”权限******/
INSERT INTO `menu_permission` VALUES ('creditor_repay_plan_list','creditor_repay_plan_list') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

/******给“充值”菜单绑定“充值”权限******/
INSERT INTO `menu_permission` VALUES ('recharge','recharge') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

/******给“手动充值”菜单绑定“手动充值”权限******/
INSERT INTO `menu_permission` VALUES ('manual_recharge','manual_recharge') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

/******给“批量充值”菜单绑定“批量充值”权限******/
INSERT INTO `menu_permission` VALUES ('batch_recharge','batch_recharge') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

/****给(系统)管理员添加“债权还款计划”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','creditor_repay_plan_list') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“充值”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','recharge') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“手动充值”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','manual_recharge') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“批量充值”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','batch_recharge') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);
COMMIT;