package com.tuotiansudai.jpush.repository.model;

public enum PushType {
    BIRTHDAY_ALERT_MONTH("AUTO","生日月加息活动提醒"),
    BIRTHDAY_ALERT_DAY("AUTO","生日提醒"),
    LOAN_ALERT("AUTO","放款提醒"),
    NO_INVEST_ALERT("AUTO","投资提醒"),

    REPAY_ALERT("AUTO","还款提醒"),
    RECHARGE_ALERT("AUTO","充值提醒"),
    WITHDRAW_APPLY_SUCCESS_ALERT("AUTO","申请提现成功提醒"),
    WITHDRAW_SUCCESS_ALERT("AUTO","提现成功提醒"),
    REFERRER_REWARD_ALERT("AUTO","推荐人奖励提醒"),

    PREHEAT("MANUAL","预热标的"),
    PRESENT_SEND("MANUAL","礼品派送"),
    IMPORTANT_EVENT("MANUAL","重大事件"),
    HUMANISTIC_CARE("MANUAL","人文关怀");



    private final String type;

    private final String description;



    PushType(String type ,String description){
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }



}
