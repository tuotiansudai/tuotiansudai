package com.tuotiansudai.fudian.dto.request;

public enum QueryTradeType {

    RECHARGE("01"), //快捷
    WITHDRAW("02"), //富滇直充
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
