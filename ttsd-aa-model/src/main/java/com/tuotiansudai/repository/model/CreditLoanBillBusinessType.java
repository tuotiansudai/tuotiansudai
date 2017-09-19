package com.tuotiansudai.repository.model;

public enum CreditLoanBillBusinessType {

    CREDIT_LOAN_TRANSFER_AGENT("信用贷给代理人转账"),
    CREDIT_LOAN_REPAY("信用贷用户还款"),
    CREDIT_LOAN_RECHARGE("信用贷账户充值");

    private final String description;

    CreditLoanBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
