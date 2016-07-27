BEGIN;

UPDATE `message`
SET `channels` = CONCAT(`channels`, ',APP_MESSAGE')
WHERE `type` = 'EVENT';

UPDATE `message`
SET `app_title` = `title`
WHERE `event_type` IN ('REGISTER_USER_SUCCESS', 'REGISTER_ACCOUNT_SUCCESS');

UPDATE `message`
SET `app_title` = '充值成功：您已成功充值{0}元，及时投资赚取更多哦。'
WHERE `event_type` = 'RECHARGE_SUCCESS';

UPDATE `message`
SET `app_title` = '提现成功：您已成功提现{0}元，选择拓天，共赢财富。'
WHERE `event_type` = 'WITHDRAW_SUCCESS';

UPDATE `message`
SET `app_title` = '投标成功：您在{0}项目成功投资{1}元，即将放款生息。'
WHERE `event_type` = 'INVEST_SUCCESS';

UPDATE `message`
SET `app_title` = '您发起的转让项目{0}转让成功，资金已经到达您的账户。'
WHERE `event_type` = 'TRANSFER_SUCCESS';

UPDATE `message`
SET `app_title` = '很遗憾，您发起的转让项目{0}没有转让成功。'
WHERE `event_type` = 'TRANSFER_FAIL';

UPDATE `message`
SET `app_title` = '您投资的{0}项目已经满额放款，即日生息。'
WHERE `event_type` = 'LOAN_OUT_SUCCESS';

UPDATE `message`
SET `app_title` = '您投资的{0}项目还款啦，赶快查看收益吧。'
WHERE `event_type` = 'REPAY_SUCCESS';

UPDATE `message`
SET `app_title` = '您推荐的好友{0}成功注册，若该好友进行投资，您即可获取现金奖励哦'
WHERE `event_type` = 'RECOMMEND_SUCCESS';

UPDATE `message`
SET `app_title` = '您的好友{0}成功进行了投资，您获得了{1}元现金奖励。'
WHERE `event_type` = 'RECOMMEND_AWARD_SUCCESS';

UPDATE `message`
SET `app_title` = '您获得了{0}，有效期{1}至{2}。'
WHERE `event_type` = 'ASSIGN_COUPON_SUCCESS';

UPDATE `message`
SET `app_title` = '您的{0}即将到期，使用可以获取额外收益哦，快去我的宝藏查看吧。'
WHERE `event_type` = 'COUPON_5DAYS_EXPIRED_ALERT';

COMMIT;
