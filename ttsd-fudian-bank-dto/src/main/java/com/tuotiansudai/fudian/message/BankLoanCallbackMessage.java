package com.tuotiansudai.fudian.message;

public class BankLoanCallbackMessage extends BankBaseMessage {

    private long investId;

    private long investRepayId;

    private long corpus;

    private long interest;

    private long defaultInterest;

    private long interestFee;

    private String bankOrderNo;

    private String bankOrderDate;

    private boolean isNormalRepay;

    public BankLoanCallbackMessage() {
    }

    public BankLoanCallbackMessage(long investId, long investRepayId, long corpus, long interest, long defaultInterest, long interestFee, String bankOrderNo, String bankOrderDate, boolean isNormalRepay) {
        this.investId = investId;
        this.investRepayId = investRepayId;
        this.corpus = corpus;
        this.interest = interest;
        this.defaultInterest = defaultInterest;
        this.interestFee = interestFee;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.isNormalRepay = isNormalRepay;
    }

    public long getInvestId() {
        return investId;
    }

    public long getInvestRepayId() {
        return investRepayId;
    }

    public long getCorpus() {
        return corpus;
    }

    public long getInterest() {
        return interest;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public long getInterestFee() {
        return interestFee;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public boolean isNormalRepay() {
        return isNormalRepay;
    }
}
