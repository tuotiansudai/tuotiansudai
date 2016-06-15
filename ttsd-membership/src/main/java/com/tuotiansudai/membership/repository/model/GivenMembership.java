package com.tuotiansudai.membership.repository.model;

public enum GivenMembership {
    NO_TIME("7月1日00：00开放领取哦亲!","/register/user"),
    NO_LOGIN("完成注册并实名认证即可免费领取会员V5超级特权哦!","/register/user"),
    NO_REGISTER("完成实名认证即可免费领取会员V5超级特权哦!","/register/account"),
    ALREADY_RECEIVED("您已经领取过超级特权了哦!",""),
    ALREADY_REGISTER_NOT_INVEST_1000("投资满1000元即可免费领取V5会员超级特权!","/loan-list"),
    ALREADY_REGISTER_ALREADY_INVEST_1000("领取成功！恭喜您成为拓天速贷会员V5，享有超级特权！快试一下吧~","/loan-list"),
    AFTER_START_ACTIVITY_REGISTER("领取成功！恭喜您成为拓天速贷会员V5，享有超级特权！快试一下吧~","/loan-list");

    private final String description;

    private final String url;

    GivenMembership(String description, String url) {
        this.description = description;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() { return url; }

    @Override
    public String toString() {
        return description + "," + url;
    }
}
