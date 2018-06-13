package com.tuotiansudai.fudian.dto;

public enum QueryTradeType {

    RECHARGE("01"), //充值
    WITHDRAW("02"), //提现
    LOAN_INVEST("03"), //投资
    LOAN_REPAY("04"), //还款
    LOAN_CALLBACK("05"), //回款
    LOAN_CREDIT_INVEST("06"), //债转
    LOAN_FULL("07"); //放款

    private String value;

    QueryTradeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
