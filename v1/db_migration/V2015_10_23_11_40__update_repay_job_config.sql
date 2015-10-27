

BEGIN ;

update config set value = '1' where id = 'schedule.enable_repay_alert';
UPDATE `user_message_template` SET `template`='今天有#{loanRepayAmount}元尚未还款，请及时登录系统还款。【拓天速贷】' WHERE `id`='repay_alert_sms';
update config set value = '0' where id = 'repay_alert.days_before';

COMMIT ;






