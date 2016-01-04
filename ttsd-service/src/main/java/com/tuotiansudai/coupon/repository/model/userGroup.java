package com.tuotiansudai.coupon.repository.model;

public enum UserGroup {
    ALL_INVEST_USER("平台内所有投资过的用户"),
    INVEST_EXCEED_LIMIT_USER("累计投资额超过10000元的用户"),
    CERTIFIED_NOT_INVEST_USER("已认证未投资用户");

    private String desc;

    UserGroup(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
