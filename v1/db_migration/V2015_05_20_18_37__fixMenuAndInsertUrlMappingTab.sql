DELETE FROM menu WHERE id IN (
		'gongSiXinXi001',
		'pingTaiYuanLi1',
		'xiangguanxieyi',
		'gongShangZhiZhao',
		'lianXiWoMen',
		'heZuoHuoBan1',
		'jiaRuWoMen',
		'anquanbaozhang'
	) ;

DELETE FROM menu WHERE id = 'aboutUs';

INSERT INTO `menu` VALUES ('intro', 'MainMenu', '关于我们', '/website/intro', null, '1', '8', '', '1', '*', '_self', '');

INSERT INTO `menu` VALUES ('introduction', 'MainMenu', '公司介绍', '/website/intro', 'intro', '1', '1', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('security', 'MainMenu', '安全保障', '/website/security', 'intro', '1', '2', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('media', 'MainMenu', '媒体介绍', '/website/media', 'intro', '1', '3', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('contact', 'MainMenu', '联系我们', '/website/contact', 'intro', '1', '4', '', '1', '*', '_self', '');


delete  from menu where pid like '%Navigation-01%';
delete  from menu where id = 'Navigation-01';

delete  from menu where pid like '%Navigation-02%';
delete  from menu where id = 'Navigation-02';

delete  from menu where pid like '%Navigation-03%';
delete  from menu where id = 'Navigation-03';

delete  from menu where pid like 'bangzhuzhongxin';
delete  from menu where id = 'bangzhuzhongxin';

INSERT INTO `menu` VALUES ('n-intro', 'Navigation', '关于我们', '/website/intro', null, '1', '0', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('n-introduction', 'Navigation', '公司介绍', '/website/intro', 'n-intro', '1', '1', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('n-security', 'Navigation', '安全保障', '/website/security', 'n-intro', '1', '2', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('n-media', 'Navigation', '媒体介绍', '/website/media', 'n-intro', '1', '3', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('n-contact', 'Navigation', '联系我们', '/website/contact', 'n-intro', '1', '4', '', '1', '*', '_self', '');

INSERT INTO `menu` VALUES ('n-center', 'Navigation', '帮助中心', '/website/helpCenter', null, '1', '2', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('n-guide', 'Navigation', '新手指引', '/website/guide', 'n-center', '1', '1', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('n-cost', 'Navigation', '网站费用', '/website/cost', 'n-center', '1', '2', '', '1', '*', '_self', '');
INSERT INTO `menu` VALUES ('n-protocol', 'Navigation', '法律声明及用户协议', '/website/protocol', 'n-center', '1', '3', '', '1', '*', '_self', '');


INSERT INTO `url_mapping` VALUES ('intro', '/website/intro', 'themepath:website/intro.htm', '关于我们');
INSERT INTO `url_mapping` VALUES ('security', '/website/security', 'themepath:website/security.htm', '安全保障');
INSERT INTO `url_mapping` VALUES ('media', '/website/media', 'themepath:website/media.htm', '媒体介绍');
INSERT INTO `url_mapping` VALUES ('contact', '/website/contact', 'themepath:website/contact.htm', '联系我们');
INSERT INTO `url_mapping` VALUES ('helpCenter', '/website/helpCenter', 'themepath:website/helpCenter.htm', '帮助中心');
INSERT INTO `url_mapping` VALUES ('guide', '/website/guide', 'themepath:website/guide.htm', '新手指引');
INSERT INTO `url_mapping` VALUES ('cost', '/website/cost', 'themepath:website/cost.htm', '网站费用');
INSERT INTO `url_mapping` VALUES ('protocol', '/website/protocol', 'themepath:website/protocol.htm', '法律声明及用户协议');












