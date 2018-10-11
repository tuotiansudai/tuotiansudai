package com.tuotiansudai.enums;

public enum MessageEventType {
    REGISTER_USER_SUCCESS("注册成功", "6888元体验金已存入您的账户，请查收！", "哇，您终于来啦！初次见面，岂能无礼？6888元体验金及1000元出借红包双手奉上，快去出借吧！"),
    NEWMAN_TYRANT("新贵富豪争霸赛", "恭喜您获得活动奖励5888元体验金！", "{0}您在“新贵富豪争霸赛”活动中获得胜利，奖励5888元体验金已发送到您的账户中。请在“我的-我的体验金”中查看！"),
    REGISTER_ACCOUNT_SUCCESS("实名认证成功", "恭喜您认证成功", "尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！"),
    WITHDRAW_APPLICATION_SUCCESS("提现申请成功", "您的{0}元提现申请已提交成功", "尊敬的用户，您提交了{0}元提现申请，联动优势将会在1个工作日内进行审批，请耐心等待。"),
    WITHDRAW_SUCCESS("提现到账", "您的{0}元提现已到账,请查收", "尊敬的用户，您提交的{0}元提现申请已成功通过审核，请及时查收款项，感谢您选择拓天速贷。"),
    RECOMMEND_SUCCESS("推荐成功", "您推荐的好友{0}已成功注册", "尊敬的用户，您推荐的好友{0}已成功注册，【邀请好友出借】您还能再拿0.5%现金奖励哦！"),
    RECOMMEND_AWARD_SUCCESS("推荐获得奖励", "{0}元推荐奖励已存入您的账户，请查收！", "尊敬的用户，您推荐的好友{0}出借成功，您已获得{1}元现金奖励。"),
    INVEST_SUCCESS("出借成功", "恭喜您成功出借{0}元", "尊敬的用户，您已成功出借房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友出借】还能额外拿0.5%现金奖励哦！"),
    LOAN_OUT_SUCCESS("放款成功", "您出借的{0}已经满额放款，预期年化收益{1}%", "尊敬的用户，您出借的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。"),
    REPAY_SUCCESS("正常回款", "您出借的{0}已回款{1}元，请前往账户查收！", "尊敬的用户，您出借的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。"),
    ADVANCED_REPAY("提前还款", "您出借的{0}提前还款，{1}元已返还至您的账户！", "尊敬的用户，您在{0}出借的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】"),
    TRANSFER_SUCCESS("债权转让成功", "您发起的转让项目转让成功，{0}元已发放至您的账户！", "尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。"),
    TRANSFER_FAIL("债权转让失败(到期取消)", "您提交的债权转让到期取消，请查看！", "尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。"),
    MEMBERSHIP_PRIVILEGE_EXPIRED("增值特权到期提醒", "您购买的增值特权已过期", "尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，请及时续费。"),
    MEMBERSHIP_UPGRADE("会员升级", "恭喜您会员等级提升至V{0}", "尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。"),
    COUPON_5DAYS_EXPIRED_ALERT("优惠券到期提醒(5天后)", "您有一张{0}即将失效", "尊敬的用户，您有一张{0}即将失效（有效期至：{1}），请尽快使用！"),
    BIRTHDAY("生日提醒", "拓天速贷为您送上生日祝福，请查收！", "尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月出借即可享受收益翻倍哦！"),
    MEMBERSHIP_PRIVILEGE_BUY_SUCCESS("增值特权购买成功", "恭喜您已成功购买{0}天增值特权", "尊敬的用户，恭喜您已成功购买增值特权，有效期至{0}日，【马上出借】享受增值特权吧！"),
    ASSIGN_COUPON_SUCCESS("获得优惠券", "恭喜您获得了一张{0}！", "尊敬的用户，恭喜您获得了一张{0}，有效期至{1}！"),
    ASSIGN_EXPERIENCE_SUCCESS("获得体验金", "{0}元活动奖励体验金已到账", "亲爱的用户，您在周年庆单笔狂欢场活动期间出借{0}元，获得{1}元体验金请注意查收。"),
    DOUBLE_ELEVEN_ACTIVITY_EVEN("双十一活动出借奇数奖励", "双十一活动出借奖励通知", "亲爱的用户，您出借的{0}项目的顺序为奇数，当项目满额放款后，我们将发送给您出借金额1.1倍的体验金奖励。"),
    DOUBLE_ELEVEN_ACTIVITY_ODD("双十一活动出借偶数奖励", "双十一活动出借奖励通知", "亲爱的用户，您出借的{0}项目的顺序为偶数，获得了一次抽奖机会，您可以前往活动页面进行抽奖。"),
    PAYROLL_HAS_BEEN_TRANSFERRED("代发工资到账", "{0}已发放到您的账户余额中，请注意查收。", "{0}已发放到您的账户余额中，请注意查收。"),
    ;

    private final String description;

    private final String titleTemplate;

    private final String contentTemplate;

    MessageEventType(String description, String titleTemplate, String contentTemplate) {
        this.description = description;
        this.titleTemplate = titleTemplate;
        this.contentTemplate = contentTemplate;
    }

    public String getDescription() {
        return description;
    }

    public String getTitleTemplate() {
        return titleTemplate;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }
}
