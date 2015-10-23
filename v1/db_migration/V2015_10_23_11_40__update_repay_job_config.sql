

BEGIN ;

update config set value = '1' where id = 'schedule.enable_repay_alert';
update user_message_template set templete = '今天有#{loanRepayAmount}元尚未还款，请及时登录系统还款。【拓天速贷】';
update config set value = '0' where id = 'repay_alert.days_before';

COMMIT ;






