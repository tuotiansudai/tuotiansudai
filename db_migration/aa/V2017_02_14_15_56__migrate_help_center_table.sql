begin;
update help_center set content = '新手体验项目仅适用于从未在平台进行过投资行为的用户。注册完成后，即可进行投资体验。用户在平台投资累计满1000元即可提现该收益（投资债权转让项目除外）'
where id = 100029;

update help_center set content = '1、注册拓天速贷平台账号后，即可获得新手优惠券。2、用户也可通过参与平台活动等获得优惠券奖励。3、连续签到8天、18天、28天、38天、58天、365天可获得优惠券'
where id = 100052;

update help_center set content = '按照投资金额年化1：1转化为成长值，不足1元的舍去不计，投资金额越多，成长值越高，会员等级就越高。'
where id = 100067;
COMMIT ;