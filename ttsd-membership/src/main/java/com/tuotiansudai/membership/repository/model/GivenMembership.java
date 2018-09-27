package com.tuotiansudai.membership.repository.model;

public enum GivenMembership {
    NO_TIME("7月1日00：00开放领取哦亲!","","返回"),
    END_TIME("活动已经结束了哦!","","返回"),
    NO_LOGIN("完成注册并实名认证即可免费领取会员V5超级特权哦!","/register/user","去注册"),
    NO_REGISTER("完成实名认证即可免费领取会员V5超级特权哦!","/register/account","去认证"),
    ALREADY_RECEIVED("您已经领取过超级特权了哦!","",""),
    ALREADY_REGISTER_NOT_INVEST_1000("出借满1000元即可免费领取V5会员超级特权!","/loan-list?productType=JYF","小投1000元"),
    ALREADY_REGISTER_ALREADY_INVEST_1000("领取成功！恭喜您成为拓天速贷会员V5，享有超级特权！快试一下吧~","/loan-list?productType=JYF","去试试"),
    AFTER_START_ACTIVITY_REGISTER("领取成功！恭喜您成为拓天速贷会员V5，享有超级特权！快试一下吧~","/loan-list?productType=JYF","去试试");

    private final String description;

    private final String url;

    private final String btnName;

    GivenMembership(String description, String url, String btnName) {
        this.description = description;
        this.url = url;
        this.btnName = btnName;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() { return url; }

    public String getBtnName() { return btnName; }
}
