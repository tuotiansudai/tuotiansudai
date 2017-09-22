package com.tuotiansudai.repository.model;

public enum CreditLoanBillBusinessType {

    CREDIT_LOAN_RECHARGE("信用贷账户充值");

    private final String description;

    CreditLoanBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
