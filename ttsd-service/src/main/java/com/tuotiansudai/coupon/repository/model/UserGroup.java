package com.tuotiansudai.coupon.repository.model;

public enum UserGroup {
    ALL_USER("全部用户"),
    INVESTED_USER("已投资用户"),
    REGISTERED_NOT_INVESTED_USER("已实名未投资用户"),
    STAFF("业务员"),
    STAFF_RECOMMEND_LEVEL_ONE("业务员一代用户"),
    AGENT("代理商"),
    CHANNEL("来源渠道"),
    NEW_REGISTERED_USER("新注册用户"),
    IMPORT_USER("导入用户名单"),
    EXCHANGER("财豆兑换用户"),
    WINNER("中奖用户"),
    EXCHANGER_CODE("兑换码"),
    MEMBERSHIP_V0("会员V0"),
    MEMBERSHIP_V1("会员V1"),
    MEMBERSHIP_V2("会员V2"),
    MEMBERSHIP_V3("会员V3"),
    MEMBERSHIP_V4("会员V4"),
    MEMBERSHIP_V5("会员V5");

    private final String description;

    UserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}