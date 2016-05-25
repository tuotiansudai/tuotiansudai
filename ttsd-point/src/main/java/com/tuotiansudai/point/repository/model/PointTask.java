package com.tuotiansudai.point.repository.model;

public enum PointTask {
    REGISTER("注册认证","完成注册并实名认证"),
    BIND_EMAIL("绑定邮箱","完成绑定邮箱"),
    BIND_BANK_CARD("绑定银行卡","绑定您的常用银行卡"),
    FIRST_RECHARGE("首次充值","完成在平台的首次充值"),
    FIRST_INVEST("首次投资","完成在平台的首次投资"),
    SUM_INVEST_10000("累计投资10000元","累计投资满10000元"),

    EVERT_SUM_INVEST("累计投资满5000×n（n+1）÷2元","累计投资满5000×n（n+1）÷2元奖励1000×n财豆"),
    EVERT_SINGLE_INVEST("单笔投资满10000×n（n-1）元","单笔投资满10000×n（n-1）元奖励1000×n财豆"),
    EVERY_INVITE_FRIEND("每邀请1名好友注册","每邀请1名好友注册奖励200财豆"),
    EVERY_INVITE_INVEST("每邀请好友投资满","每邀请好友投资满1000元奖励1000财豆"),
    FIRST_INVITE_FRIEND("首次邀请好友投资","首次邀请好友投资奖励5000财豆"),
    FIRST_INVEST_180D("首次投资180天标的","首次投资180天标的奖励1000财豆"),
    FIRST_NO_PASSWORD_INVEST("首次开通免密支付并成功投资","首次开通免密支付并成功投资奖励1000财豆"),
    FIRST_AUTO_INVEST("首次开通并成功自动投标的","首次开通并成功自动投标奖励1000财豆"),
    FIRST_INVEST_360D("首次投资360天标的","首次投资360天标的奖励1000财豆");


    private final String title;
    private final String description;

    PointTask(String title,String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
