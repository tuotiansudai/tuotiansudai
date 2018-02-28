package com.tuotiansudai.repository.model;

public enum CreditLoanBillBusinessType {

    // 出账
    CREDIT_LOAN_OFFER("发放信用贷用户借款"),
    CREDIT_LOAN_RETRIEVE("发放代理人账户"),

    // 入账
    CREDIT_LOAN_REPAY("信用贷用户还款"),
    CREDIT_LOAN_RECHARGE("信用贷代理人账户充值"),
    CREDIT_LOAN_ACTIVATE_ACCOUNT("信用贷一分钱激活账户"),
    YOOCAR_LOAN_REPAY("优车贷用户还款"),
    LUXURY_STAGE_REPAY("奢侈品分期");

    private final String description;

    CreditLoanBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
