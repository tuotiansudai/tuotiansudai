package com.tuotiansudai.repository.model;

public enum InvestStatus {

    WAIT_PAY("等待支付"),

    SUCCESS("投资成功"),

    FAIL("投资失败"),

    OVER_INVEST_PAYBACK("超投返款"),

    OVER_INVEST_PAYBACK_FAIL("超投返款失败"),

    CANCEL_INVEST_PAYBACK("流标返款");

    private final String description;

    InvestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
