insert aa.jpush_alert(name,push_type,push_source,status,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-还款','REPAY_ALERT','ALL','DISABLED','亲爱的天宝，您刚刚收到一笔{0}元的项目还款，请点击查看','INVEST_RECEIVABLES','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,status,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-充值','RECHARGE_ALERT','ALL','DISABLED','亲爱的天宝，恭喜您成功充值了{0}元，当前账户总余额为{1}元','MY_WEALTH','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,status,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-申请提现成功','WITHDRAW_APPLY_SUCCESS_ALERT','ALL','DISABLED','亲爱的天宝，您提现{0}元已经申请成功，联动优势将会在1个工作日内进行审批打款','WITHDRAW_RECORDS','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,status,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-提现成功','WITHDRAW_SUCCESS_ALERT','ALL','DISABLED','亲爱的天宝，您提现{0}元的申请已经通过联动优势的审核并已打款，请您在今日注意查收','MY_INVEST','',now(),1);

insert aa.jpush_alert(name,push_type,push_source,status,content,jump_to,jump_to_link,created_time,is_automatic)
value('用户资金变动推送-推荐人奖励','REFERRER_REWARD_ALERT','ALL','DISABLED','亲爱的天宝，恭喜您收到来自{0}的投资奖励{1}元，当前账户总余额为{2}元','REFERRER_INVEST_LIST','',now(),1);
