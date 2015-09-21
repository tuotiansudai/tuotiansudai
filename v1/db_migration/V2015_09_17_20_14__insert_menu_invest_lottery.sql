BEGIN ;

INSERT INTO `url_mapping` (`id`,`pattern`,`view_id`,`description`) VALUES ('investLottery','/user/my_invest_lottery','themepath:user/my_invest_lottery.htm','');

INSERT INTO `menu` (`id`,`type`,`label`,`url`,`pid`,`enable`,`seq_num`,`description`,`expanded`,`language`,`target`,`icon`) VALUES ('investLottery_msg','Management','抽奖管理','','marketing_mgr','1',0,'','1','*','_self','');
INSERT INTO `menu` (`id`,`type`,`label`,`url`,`pid`,`enable`,`seq_num`,`description`,`expanded`,`language`,`target`,`icon`) VALUES ('investLottery_query','Management','抽奖查询','/admin/user/investLotteryList.htm','investLottery_msg','1',0,'','1','*','_self','');


COMMIT ;