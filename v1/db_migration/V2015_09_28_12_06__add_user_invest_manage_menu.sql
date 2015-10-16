BEGIN;
/** 给 menu_permission 表增加主键 **/
alter table menu_permission add primary key (`menu_id`,`permission_id`);

COMMIT;


BEGIN ;

/**增加客服角色**/
INSERT INTO `role` VALUES ('custorm-service','客服','客服') ON duplicate KEY UPDATE `name`=values(`name`),`description`=values(`description`);

/**增加用户投资管理权限**/
INSERT INTO `permission` VALUES ('user_invest', '用户投资管理', '用户投资管理') ON duplicate KEY UPDATE `name`=values(`name`),`description`=values(`description`);

/**给管理员和客服角色增加新权限**/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR', 'user_invest') ON duplicate KEY update role_id=values(role_id),permission_id=values(permission_id);
INSERT INTO `role_permission` VALUES ('custorm-service', 'user_invest') ON duplicate KEY update role_id=values(role_id),permission_id=values(permission_id);

/**增加用户投资管理菜单**/
INSERT INTO `menu` VALUES ('user_invest','Management','用户投资管理','/admin/user/userInvest.htm','user_mgr_2','1','1','用户投资管理','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);

/**设置菜单的访问权限**/
INSERT INTO `menu_permission` VALUES ('user_invest','user_invest') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

COMMIT ;
