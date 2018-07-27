package com.tuotiansudai.fudian.umpmessage;

import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpRepayPaybackMessage extends BankBaseMessage {

    private long loanId;

    private long investId;

    private String loginName;

    private long investRepayId;

    private long corpus;

    private long interest;

    private long fee;

    private long defaultInterest;

    private boolean isNormalRepay;

    public UmpRepayPaybackMessage() {
    }

    public UmpRepayPaybackMessage(String loginName, long loanId, long investId, long investRepayId, long corpus, long interest, long fee, long defaultInterest, boolean isNormalRepay) {
        this.loginName = loginName;
        this.loanId = loanId;
        this.investId = investId;
        this.investRepayId = investRepayId;
        this.corpus = corpus;
        this.interest = interest;
        this.fee = fee;
        this.defaultInterest = defaultInterest;
        this.isNormalRepay = isNormalRepay;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getInvestId() {
        return investId;
    }

    public String getLoginName() {
        return loginName;
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

    public long getFee() {
        return fee;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public boolean isNormalRepay() {
        return isNormalRepay;
    }
}
