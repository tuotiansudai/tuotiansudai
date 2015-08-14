BEGIN ;
/****给(系统)管理员添加“债权还款计划”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','creditor_repay_plan_list') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“充值”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','recharge') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“手动充值”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','manual_recharge') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“批量充值”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','batch_recharge') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);
COMMIT ;