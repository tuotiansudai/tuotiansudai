BEGIN;


UPDATE message
SET title  = '6888元体验金已存入您的账户，请查收！',
  template = '哇，您终于来啦！初次见面，岂能无礼？6888元体验金及668元现金红包双手奉上，快去投资吧！',
  template_txt = '哇，您终于来啦！初次见面，岂能无礼？6888元体验金及668元现金红包双手奉上，快去投资吧！'
WHERE event_type = 'REGISTER_USER_SUCCESS';

UPDATE message
SET title  = '恭喜您获得活动奖励5888元体验金！',
  template = '{0}您在“新贵富豪争霸赛”活动中获得胜利，奖励5888元体验金已发送到您的账户中。请在“我的-我的体验金”中查看！',
  template_txt = '{0}您在“新贵富豪争霸赛”活动中获得胜利，奖励5888元体验金已发送到您的账户中。请在“我的-我的体验金”中查看！'
WHERE event_type = 'NEWMAN_TYRANT';

UPDATE message
SET title  = '恭喜您认证成功',
  template = '尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！',
  template_txt = '尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！'
WHERE event_type = 'REGISTER_ACCOUNT_SUCCESS';

UPDATE message
SET title  = '您的{0}元提现申请已提交成功',
  template = '尊敬的用户，您提交了{0}元提现申请，联动优势将会在1个工作日内进行审批，请耐心等待。',
  template_txt = '尊敬的用户，您提交了{0}元提现申请，联动优势将会在1个工作日内进行审批，请耐心等待。'
WHERE event_type = 'WITHDRAW_APPLICATION_SUCCESS';

UPDATE message
SET title  = '您的{0}元提现已到账,请查收',
  template = '尊敬的用户，您提交的{0}元提现申请已成功通过审核，请及时查收款项，感谢您选择拓天速贷。',
  template_txt = '尊敬的用户，您提交的{0}元提现申请已成功通过审核，请及时查收款项，感谢您选择拓天速贷。'
WHERE event_type = 'WITHDRAW_SUCCESS';

UPDATE message
SET title  = '您推荐的好友{0}已成功注册',
  template = '尊敬的用户，您推荐的好友{0}已成功注册，【邀请好友投资】您还能再拿0.5%现金奖励哦！',
  template_txt = '尊敬的用户，您推荐的好友{0}已成功注册，【邀请好友投资】您还能再拿0.5%现金奖励哦！'
WHERE event_type = 'RECOMMEND_SUCCESS';

UPDATE message
SET title  = '{0}元推荐奖励已存入您的账户，请查收！',
  template = '尊敬的用户，您推荐的好友{0}投资成功，您已获得{1}元现金奖励。',
  template_txt = '尊敬的用户，您推荐的好友{0}投资成功，您已获得{1}元现金奖励。'
WHERE event_type = 'RECOMMEND_AWARD_SUCCESS';

UPDATE message
SET title  = '恭喜您成功投资{0}元',
  template = '尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿0.5%现金奖励哦！',
  template_txt = '尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿0.5%现金奖励哦！',
  app_url = 'MY_INVEST_RAISING'
WHERE event_type = 'INVEST_SUCCESS';

UPDATE message
SET title  = '您投资的{0}已经满额放款，预期年化收益{1}%',
  template = '尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。',
  template_txt = '尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。'
WHERE event_type = 'LOAN_OUT_SUCCESS';

UPDATE message
SET title  = '您投资的{0}已回款{1}元，请前往账户查收！',
  template = '尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。',
  template_txt = '尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。'
WHERE event_type = 'REPAY_SUCCESS';

UPDATE message
SET title  = '您投资的{0}提前还款，{1}元已返还至您的账户！',
  template = '尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】',
  template_txt = '尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】',
  app_url= 'MY_INVEST_FINISH'
WHERE event_type = 'ADVANCED_REPAY';

UPDATE message
SET title  = '您发起的转让项目转让成功，{0}元已发放至您的账户！',
  template = '尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。',
  template_txt = '尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。'
WHERE event_type = 'TRANSFER_SUCCESS';

UPDATE message
SET title  = '您提交的债权转让到期取消，请查看！',
  template = '尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。',
  template_txt = '尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。'
WHERE event_type = 'TRANSFER_FAIL';

UPDATE message
SET title  = '您购买的增值特权已过期',
  template = '尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，请及时续费。',
  template_txt = '尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，请及时续费。'
WHERE event_type = 'MEMBERSHIP_PRIVILEGE_EXPIRED';

UPDATE message
SET title  = '恭喜您会员等级提升至V{0}',
  template = '尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。',
  template_txt = '尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。'
WHERE event_type = 'MEMBERSHIP_UPGRADE';

UPDATE message
SET title  = '您有一张{0}即将失效',
  template = '尊敬的用户，您有一张{0}即将失效（有效期至：{1}），请尽快使用！',
  template_txt = '尊敬的用户，您有一张{0}即将失效（有效期至：{1}），请尽快使用！'
WHERE event_type = 'COUPON_5DAYS_EXPIRED_ALERT';

UPDATE message
SET title  = '拓天速贷为您送上生日祝福，请查收！',
  template = '尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月投资即可享受收益翻倍哦！',
  template_txt = '尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月投资即可享受收益翻倍哦！'
WHERE event_type = 'BIRTHDAY';

UPDATE message
SET title  = '恭喜您获得了一张{0}！',
  template = '尊敬的用户，恭喜您获得了一张{0}，有效期至{1}！',
  template_txt = '尊敬的用户，恭喜您获得了一张{0}，有效期至{1}！'
WHERE event_type = 'ASSIGN_COUPON_SUCCESS';

UPDATE message
SET title  = '{0}元活动奖励体验金已到账',
  template = '亲爱的用户，您在周年庆单笔狂欢场活动期间投资{0}元，获得{1}元体验金请注意查收。',
  template_txt = '亲爱的用户，您在周年庆单笔狂欢场活动期间投资{0}元，获得{1}元体验金请注意查收。'
WHERE event_type = 'ASSIGN_EXPERIENCE_SUCCESS';

UPDATE message
SET title  = '恭喜您已成功购买{0}天增值特权',
  template = '尊敬的用户，恭喜您已成功购买增值特权，有效期至{0}日，【马上投资】享受增值特权吧！',
  template_txt = '尊敬的用户，恭喜您已成功购买增值特权，有效期至{0}日，【马上投资】享受增值特权吧！'
WHERE event_type = 'MEMBERSHIP_PRIVILEGE_BUY_SUCCESS';


COMMIT;