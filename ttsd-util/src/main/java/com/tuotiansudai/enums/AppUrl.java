package com.tuotiansudai.enums;

public enum AppUrl {
    LOGIN("登录", "app/tuotian/login"),
    MY_ASSESS("我的主页面", "app/tuotian/myfund"),
    MY_ACCOUNT("个人资料", "app/tuotian/myAccount"),
    MY_FUND_AVAILABLE("账户余额","app/tuotian/myfund-available"),
    SETTING("设置", "app/tuotian/setting"),
    FAST_PAY("开通快捷支付页面", "app/tuotian/open-fastpay"),
    REAL_NAME_IDENTITY("实名认证", "app/tuotian/authentication"),
    FUND_DETAIL_LIST("资金明细", "app/tuotian/fund-detail-list"),
    POINT_SHOP_HOME("积分商城首页", "app/tuotian/point-home"),
    MY_TREASURE_UNUSED("优惠券>未使用", "app/tuotian/myfortune-unuse"),
    MY_MEMBERSHIP("会员中心首页", "app/tuotian/vip-center"),
    MY_INVEST_REPAYING("我的出借>回款中", "app/tuotian/myinvest-repaying"),
    MY_INVEST_RAISING("我的出借>投标中", "app/tuotian/myinvest-raising"),
    MY_INVEST_FINISH("我的出借>已完成", "app/tuotian/myinvest-finish"),
    ACTIVITY_CENTER_CURRENT("发现>活动中心", "app/tuotian/activity-center"),
    INVEST_NORMAL("出借列表页>直投项目", "app/tuotian/invest-list"),
    INVEST_TRANSFER("出借列表页>转让项目", "app/tuotian/transfer-list"),
    HOME("首页主页面", "app/tuotian/home"),
    MESSAGE_CENTER_LIST("消息中心列表页", "app/tuotian/message-list"),
    REFER_REWARD("首页>邀请好友", "app/tuotian/refer-reward"),
    RECOMMEND_MY_REWARD("邀请好友>我的奖励", "app/tuotian/refer-reward-list"),
    SHARE("分享弹框", "app/tuotian/share?param=%s"),
    RISK_ESTIMATE("出借偏好测评", "app/tuotian/risk-estimate"),
    OTHER("其他", ""),
    NONE("无", null),

    //new
    INVEST_NORMAL_EXPERIENCE("出借列表页>直投项目>体验金项目详情", "app/tuotian/invest-normal-experience"),
    FUND_DETAIL_SPENDING("资金明细>支出", "app/tuotian/fund-detail-spending"),
    MY_INVEST_DETAIL("我的出借>出借详情", "app/tuotian/repayplan?isTransferInvest={0}&loanStatus={1}&investid={2}&transferApplicationId={3}&transferStatus={4}"),
    RECOMMEND_LIST("邀请好友>我的奖励>推荐人列表","app/tuotian/recommend-list"),
    CASH_WITHDRAWAL_LIST("提现记录","app/tuotian/cash-withdrawal-list");

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