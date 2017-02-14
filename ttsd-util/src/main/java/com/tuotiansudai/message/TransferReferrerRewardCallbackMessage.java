package com.tuotiansudai.message;


public class TransferReferrerRewardCallbackMessage {

    private Long loanId;

    private Long investId;

    private String loginName;

    private String referrer;

    private Long referrerRewardId;

    public TransferReferrerRewardCallbackMessage() {
    }

    public TransferReferrerRewardCallbackMessage(Long loanId, Long investId, String loginName, String referrer, Long referrerRewardId) {
        this.loanId = loanId;
        this.investId = investId;
        this.loginName = loginName;
        this.referrer = referrer;
        this.referrerRewardId = referrerRewardId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
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

    public Long getReferrerRewardId() {
        return referrerRewardId;
    }

    public void setReferrerRewardId(Long referrerRewardId) {
        this.referrerRewardId = referrerRewardId;
    }
}
