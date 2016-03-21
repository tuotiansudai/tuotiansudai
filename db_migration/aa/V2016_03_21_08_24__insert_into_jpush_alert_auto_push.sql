insert aa.jpush_alert(name,push_type,push_source,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-还款','REPAY_ALERT','ALL','亲爱的天宝，您刚刚收到一笔{}元的项目还款，请点击查看','','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-充值','RECHARGE_ALERT','ALL','亲爱的天宝，恭喜您成功充值了{}元，当前账户总余额为{}元','','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-申请提现成功','WITHDRAW_APPLY_SUCCESS_ALERT','ALL','亲爱的天宝，您提现{}元已经申请成功，联动优势将会在1个工作日内进行审批打款','','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-提现成功','WITHDRAW_SUCCESS_ALERT','ALL','亲爱的天宝，您提现{}元的申请已经通过联动优势的审核并已打款，请您在今日注意查收','','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-推荐人奖励','REPAY_ALERT','ALL','亲爱的天宝，恭喜您收到来自{realname}的投资奖励{}元，当前账户总余额为{}元','','',now(),1);
