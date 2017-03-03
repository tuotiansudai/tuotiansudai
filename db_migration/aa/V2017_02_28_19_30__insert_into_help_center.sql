begin;
INSERT INTO `aa`.`help_center` (`title`, `content`, `category`, `hot`) VALUES (
'体验金如何投资？', '用户可以用自己的体验金投资拓天体验金项目，到期后体验金由平台收回，产生收益归用户所有。', 'INVEST_REPAY', false);

INSERT INTO `aa`.`help_center` (`title`, `content`, `category`, `hot`) VALUES (
'如何查看自己的体验金？', '进入我的-我的体验金，可查看自己的可用体验金余额及体验金明细信息。', 'INVEST_REPAY', false);

INSERT INTO `aa`.`help_center` (`title`, `content`, `category`, `hot`) VALUES (
'体验金投资的收益为什么不能提现？', '为防止不法分子恶意刷取平台奖励，体验金的收益，需要在平台投资直投项目累计满1000元，才可以提现。', 'INVEST_REPAY', false);

DELETE FROM `aa`.`help_center` where title = '新手体验项目如何进行投资？';

commit;