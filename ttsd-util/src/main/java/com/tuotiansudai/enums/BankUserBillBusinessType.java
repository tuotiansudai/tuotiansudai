package com.tuotiansudai.enums;

public enum BankUserBillBusinessType {
    RECHARGE_SUCCESS("充值成功"),
    WITHDRAW_SUCCESS("提现成功"),
    INVEST_SUCCESS("投资成功"),
    INVEST_TRANSFER_OUT("债权转让"),
    INVEST_TRANSFER_IN("债权购买"),
    LOAN_SUCCESS("放款成功"),
    NORMAL_REPAY("正常还款"),
    ADVANCE_REPAY("提前还款"),
    OVERDUE_REPAY("逾期还款"),
    INVEST_FEE("利息管理费"),
    TRANSFER_FEE("债权转让管理费"),
    CANCEL_INVEST_PAYBACK("流标返款"),
    ;

    private final String description;

    BankUserBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
