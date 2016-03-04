package com.tuotiansudai.point.repository.model;

public enum PointTask {
    REGISTER("注册认证","完成注册并实名认证"),
    BIND_EMAIL("绑定邮箱","完成绑定邮箱"),
    BIND_BANK_CARD("绑定银行卡","绑定您的常用银行卡"),
    FIRST_RECHARGE("首次充值","完成在平台的首次充值"),
    FIRST_INVEST("首次投资","完成在平台的首次投资"),
    SUM_INVEST_10000("累计投资10000元","累计投资满10000元");

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
