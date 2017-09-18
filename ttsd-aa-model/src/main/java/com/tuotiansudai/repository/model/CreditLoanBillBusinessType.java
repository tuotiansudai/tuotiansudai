package com.tuotiansudai.repository.model;

public enum CreditLoanBillBusinessType {

    CREDIT_LOAN_RECHARGE("信用贷标的账户充值"),
    CREDIT_LOAN_OUT("信用贷标的账户放款"),
    XYD_USER_REPAY("信用贷用户还款");

    private final String description;

    CreditLoanBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
