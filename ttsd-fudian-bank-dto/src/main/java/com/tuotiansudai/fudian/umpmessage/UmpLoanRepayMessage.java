package com.tuotiansudai.fudian.umpmessage;

import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpLoanRepayMessage extends BankBaseMessage {

    private long loanId;

    private long loanRepayId;

    private long amount;

    private String loginName;

    private boolean isNormalRepay;

    public UmpLoanRepayMessage() {
    }

    public UmpLoanRepayMessage(long loanId, long loanRepayId, long amount, String loginName, boolean isNormalRepay) {
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.amount = amount;
        this.loginName = loginName;
        this.isNormalRepay = isNormalRepay;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getAmount() {
        return amount;
    }

    public String getLoginName() {
        return loginName;
    }

    public boolean isNormalRepay() {
        return isNormalRepay;
    }
}
