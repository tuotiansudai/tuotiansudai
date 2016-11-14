package com.tuotiansudai.enums;

public enum AppUrl {
    HOME("首页", "app://tuotian/home"),
    ACTIVITY_CENTER_CURRENT("活动中心-当前活动", "app://tuotian/activity-center"),
    RECOMMEND_DETAIL("推荐送现金详情页", "app://tuotian/refer-reward"),
    RECOMMEND_MY_REWARD("推荐送现金-我的奖励", "app://tuotian/refer-reward-list"),
    SECURITY_DETAIL("安全保障详情页", "app://tuotian/guarantee"),
    INVEST_NORMAL("投资列表页-直投项目", "app://tuotian/invest-list"),
    INVEST_TRANSFER("投资列表页-转让项目", "app://tuotian/transfer-list"),
    MY_ASSESS("我的财富", "app://tuotian/myfortune"),
    ASSESS_ADMINISTER("资金管理", "app://tuotian/myfund"),
    MY_INVEST_REPAYING("我的投资收款中", "app://tuotian/myinvest-repaying"),
    MY_INVEST_RAISING("我的投资投标中", "app://tuotian/myinvest-raising"),
    MY_INVEST_FINISH("我的投资已完成", "app://tuotian/myinvest-finish"),
    TRANSFER_HISTORY("债权转让-转让记录", "app://tuotian/mytransfer-record"),
    TRANSFER_UNDERTAKE("债权转让-已承接", "app://tuotian/mytransfer-accept"),
    MY_MEMBERSHIP("会员中心", "app://tuotian/vip-center"),
    MY_TREASURE_UNUSED("我的宝藏-未使用", "app://tuotian/myfortune-unuse"),
    POINT_SHOP_HOME("积分商城首页", "app://tuotian/point-home"),
    POINT_SHOP_POINT_BILL("积分商城-积分明细", "app://tuotian/point-record"),
    POINT_SHOP_MISSION("积分商城-积分任务", "app://tuotian/point-task"),
    POINT_SHOP_PRODUCT_BILL("积分商城-兑换记录", "app://tuotian/point-exchange-record"),
    PERSON_CENTER_HOME("个人中心-首页", "app://tuotian/myinfo-home"),
    FAST_PAY("开通快捷支付页面", "app://tuotian/open-fastpay"),
    REAL_NAME_IDENTITY("实名认证", "app://tuotian/authentication"),
    PASSWORD_ADMINISTER("密码管理页面", "app://tuotian/password-manage"),
    MEDIA_CENTER("媒体中心", "app://tuotian/media-center"),
    REGISTER("注册", "app://tuotian/register"),
    LOGIN("登录", "app://tuotian/login"),
    INVEST_DETAILS_REPAY_DETAIL("投资详情(回款计划)", "app://tuotian/myinvest-repay-plan?investId={0}"),

    MESSAGE_CENTER_LIST("消息中心列表页", "app://tuotian/message-list"),
    MESSAGE_CENTER_DETAIL("消息中心详情页", "app://tuotian/message-detail/{0}"),
    CONTACT_US("联系我们", "app://tuotian/contact-us"),
    NOTIFY("拓天公告", "app://tuotian/notify"),
    FAQ("常见问题", "app://tuotian/faq"),
    ADVICE_FEEDBACK("意见反馈", "app://tuotian/feedback"),
    RECHARGE("充值", "app://tuotian/recharge"),
    MY_TREASURE_USED("我的宝藏-已使用", "app://tuotian/my-treasure-used"),
    MY_TREASURE_OUTDATED("我的宝藏-已过期", "app://tuotian/my-treasure-expired"),
    TRANSFER_TRANSFERABLE("债权转让-可转让", "app://tuotian/transfer-transferable"),
    TRANSFER_TRANSFERRING("债权转让-转让中", "app://tuotian/transfer-transferring"),
    LOAN_NORMAL_DETAIL("标的详情页", "app://tuotian/loan-detail/{0}"),
    LOAN_TRANSFER_DETAIL("转让标的详情页", "app://tuotian/transfer-loan-detail/{0}"),
    ACTIVITY_CENTER_HISTORY("活动中心-往期活动", "app://tuotian/activity-center-history"),
    REPAY_CALENDAR("回款日历", "app://tuotian/repay-calendar");

    private String description;

    private String path;

    AppUrl(String description, String path) {
        this.description = description;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }
}