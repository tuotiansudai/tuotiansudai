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
    NOT_ENOUGH_BALANCE("账户余额不足");

    private final String description;

    InvestExceptionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
