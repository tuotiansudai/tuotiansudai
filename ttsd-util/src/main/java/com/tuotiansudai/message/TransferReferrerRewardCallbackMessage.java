package com.tuotiansudai.message;


public class TransferReferrerRewardCallbackMessage {

    private long loanId;

    private long investId;

    private String loginName;

    private String referrer;

    private long referrerRewardId;

    public TransferReferrerRewardCallbackMessage(){}

    public TransferReferrerRewardCallbackMessage(long loanId, long investId, String loginName, String referrer, long referrerRewardId) {
        this.loanId = loanId;
        this.investId = investId;
        this.loginName = loginName;
        this.referrer = referrer;
        this.referrerRewardId = referrerRewardId;
    }

    public long getReferrerRewardId() {
        return referrerRewardId;
    }

    public void setReferrerRewardId(long referrerRewardId) {
        this.referrerRewardId = referrerRewardId;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}
