BEGIN;
/******添加“推荐层级-收益比例管理(用户)”菜单******/
INSERT INTO `menu` VALUES ('refer_grade_profit_user','Management','推荐层级-收益比例管理(用户)','/admin/user/referGradeProfitListUser.htm','user_mgr_2','1','8','','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);

/******添加“用户推荐层级-收益比例管理(系统)”菜单******/
INSERT INTO `menu` VALUES ('refer_grade_profit_sys','Management','用户推荐层级-收益比例管理(系统)','/admin/user/referGradeProfitListSysInvest.htm','user_mgr_2','1','9','','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);

/******添加“业务员推荐层级-收益比例管理(系统)”菜单******/
INSERT INTO `menu` VALUES ('refer_grade_profit_Merchandiser','Management','业务员推荐层级-收益比例管理(系统)','/admin/user/referGradeProfitListSysMerchandiser.htm','user_mgr_2','1','10','','1','*','_self','') on duplicate key update type=values(type),label=values(label),url=values(url),pid=values(pid),enable=values(enable),seq_num=values(seq_num),expanded=values(expanded),language=values(language),target=values(target);


/******添加“推荐层级-收益比例管理(用户)”权限******/
INSERT INTO `permission` VALUES ('refer_grade_profit_user','推荐层级-收益比例管理(用户)','推荐层级-收益比例管理(用户)') on duplicate key update `name`=values(`name`),description=values(description);

/******添加“用户推荐层级-收益比例管理(系统)”权限******/
INSERT INTO `permission` VALUES ('refer_grade_profit_sys','用户推荐层级-收益比例管理(系统)','用户推荐层级-收益比例管理(系统)') on duplicate key update `name`=values(`name`),description=values(description);

/******添加“业务员推荐层级-收益比例管理(系统)”权限******/
INSERT INTO `permission` VALUES ('refer_grade_profit_Merchandiser','业务员推荐层级-收益比例管理(系统)','业务员推荐层级-收益比例管理(系统)') on duplicate key update `name`=values(`name`),description=values(description);


/******给“推荐层级-收益比例管理(用户)”菜单绑定“推荐层级-收益比例管理(用户)”权限******/
INSERT INTO `menu_permission` VALUES ('refer_grade_profit_user','refer_grade_profit_user') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

/******给“用户推荐层级-收益比例管理(系统)”菜单绑定“用户推荐层级-收益比例管理(系统)”权限******/
INSERT INTO `menu_permission` VALUES ('refer_grade_profit_sys','refer_grade_profit_sys') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);

/******给“业务员推荐层级-收益比例管理(系统)”菜单绑定“业务员推荐层级-收益比例管理(系统)”权限******/
INSERT INTO `menu_permission` VALUES ('refer_grade_profit_Merchandiser','refer_grade_profit_Merchandiser') on duplicate key update menu_id=values(menu_id),permission_id=values(permission_id);


/****给(系统)管理员添加“推荐层级-收益比例管理(用户)”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','refer_grade_profit_user') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“用户推荐层级-收益比例管理(系统)”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','refer_grade_profit_sys') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);

/****给(系统)管理员添加“业务员推荐层级-收益比例管理(系统)”权限****/
INSERT INTO `role_permission` VALUES ('ADMINISTRATOR','refer_grade_profit_Merchandiser') on duplicate key update role_id=values(role_id),permission_id=values(permission_id);
COMMIT;