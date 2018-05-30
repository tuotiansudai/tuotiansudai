package com.tuotiansudai.fudian.message;

public class BankLoanCallbackMessage extends BankBaseMessage {

    private long investId;

    private long investRepayId;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankLoanCallbackMessage() {
    }

    public BankLoanCallbackMessage(long investId, long investRepayId, String bankOrderNo, String bankOrderDate) {
        this.investId = investId;
        this.investRepayId = investRepayId;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public long getInvestId() {
        return investId;
    }

    public long getInvestRepayId() {
        return investRepayId;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
