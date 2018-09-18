package com.tuotiansudai.exception;

public enum InvestExceptionType {
    LOAN_NOT_FOUND("标的不存在"),
    MORE_THAN_MAX_INVEST_AMOUNT("投资金额超过了用户投资限额"),
    LOAN_IS_FULL("标的已满"),
    EXCEED_MONEY_NEED_RAISED("标的可投金额不足"),
    ILLEGAL_INVEST_AMOUNT("投资金额不符合递增金额要求"),
    LESS_THAN_MIN_INVEST_AMOUNT("投资金额小于标的最小投资金额"),
    OUT_OF_NOVICE_INVEST_LIMIT("新手标投资已超上限"),
    PASSWORD_INVEST_OFF("尚未开启免密投资"),
    ILLEGAL_LOAN_STATUS("标的暂不可投资"),
    INVESTOR_IS_LOANER("您不能接手自己的债权"),
    NOT_ENOUGH_BALANCE("账户余额不足"),
    COUPON_IS_UNUSABLE("优惠券不可用，请重新选择"),
    ANXIN_SIGN_IS_UNUSABLE("安心签电子签章服务未开启"),
    NONE_COUPON_SELECTED("使用优惠券可以增加您的收益，请选择优惠券"),
    NOT_USE_COUPON("该标的不能使用任何优惠券"),
    RISK_ESTIMATE_UNUSABLE("尚未进行风险等级评测"),
    RISK_ESTIMATE_LEVEL_OVER("该项目超出用户的风险等级"),
    RISK_ESTIMATE_AMOUNT_OVER("出借金额超出风险等级金额限制");

    private final String description;

    InvestExceptionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
