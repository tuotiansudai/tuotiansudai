package com.tuotiansudai.repository.model;

public enum UserGroup {
    ALL_USER("全部用户"),
    INVESTED_USER("已出借用户"),
    REGISTERED_NOT_INVESTED_USER("已实名未出借用户"),
    NOT_ACCOUNT_NOT_INVESTED_USER("已注册未出借用户"),
    STAFF("业务员"),
    STAFF_RECOMMEND_LEVEL_ONE("业务员一代用户"),
    AGENT("代理商"),
    CHANNEL("来源渠道"),
    NEW_REGISTERED_USER("新注册用户"),
    IMPORT_USER("导入用户名单"),
    EXCHANGER("积分兑换用户"),
    WINNER("中奖用户"),
    WINNER_NOTIFY("中奖用户-发送提醒"),
    EXCHANGER_CODE("兑换码"),
    MEMBERSHIP_V0("会员V0"),
    MEMBERSHIP_V1("会员V1"),
    MEMBERSHIP_V2("会员V2"),
    MEMBERSHIP_V3("会员V3"),
    MEMBERSHIP_V4("会员V4"),
    MEMBERSHIP_V5("会员V5"),
    EXPERIENCE_INVEST_SUCCESS("新手体验标出借用户"),
    EXPERIENCE_REPAY_SUCCESS("新手体验标收益用户"),
    FIRST_INVEST_ACHIEVEMENT("拓荒先锋"),
    MAX_AMOUNT_ACHIEVEMENT("拓天标王"),
    LAST_INVEST_ACHIEVEMENT("一锤定音");

    private final String description;

    UserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}