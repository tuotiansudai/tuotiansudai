package com.tuotiansudai.message.repository.model;

public enum AppUrl {
    HOME("首页"),
    MESSAGE_CENTER_LIST("消息中心列表页"),
    MESSAGE_CENTER_DETAIL("消息中心详情页"),
    ACTIVITY_CENTER_CURRENT("活动中心-当前活动"),
    ACTIVITY_CENTER_HISTORY("活动中心-往期活动"),
    RECOMMEND_DETAIL("推荐送现金详情页"),
    RECOMMEND_MY_REWARD("推荐送现金-我的奖励"),
    SECURITY_DETAIL("安全保障详情页"),
    INVEST_NORMAL("投资列表页-直投项目"),
    INVEST_TRANSFER("投资列表页-转让项目"),
    LOAN_NORMAL_DETAIL("标的详情页"),
    LOAN_TRANSFER_DETAIL("转让标的详情页"),
    MY_ASSESS("我的财富"),
    ASSESS_ADMINISTER("资金管理"),
    MY_INVEST_REPAYING("我的投资收款中"),
    MY_INVEST_RAISING("我的投资投标中"),
    MY_INVEST_FINISH("我的投资已完成"),
    TRANSFER_TRANSFERABLE("债权转让-可转让"),
    TRANSFER_TRANSFERRING("债权转让-转让中"),
    TRANSFER_HISTORY("债权转让-转让记录"),
    TRANSFER_UNDERTAKE("债权转让-已承接"),
    REPAY_CALENDAR("回款日历"),
    MY_MEMBERSHIP("会员中心"),
    MY_TREASURE_UNUSED("我的宝藏-未使用"),
    MY_TREASURE_USED("我的宝藏-已使用"),
    MY_TREASURE_OUTDATED("我的宝藏-已过期"),
    POINT_SHOP_HOME("积分商城首页"),
    POINT_SHOP_POINT_BILL("积分商城-积分明细"),
    POINT_SHOP_MISSION("积分商城-积分任务"),
    POINT_SHOP_PRODUCT_BILL("积分商城-兑换记录"),
    PERSON_CENTER_HOME("个人中心-首页"),
    FAST_PAY("开通快捷支付页面"),
    RECHARGE("充值"),
    REAL_NAME_IDENTITY("实名认证"),
    PASSWORD_ADMINISTER("密码管理页面"),
    CONTACT_US("联系我们"),
    NOTIFY("拓天公告"),
    MEDIA_CENTER("媒体中心"),
    FAQ("常见问题"),
    ADVICE_FEEDBACK("意见反馈"),
    REGISTER("注册"),
    LOGIN("登录"),
    ACTIVITY_CENTER("活动中心");

    private String description;

    AppUrl(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}