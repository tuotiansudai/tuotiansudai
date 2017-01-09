package com.tuotiansudai.message;


public class TransferRedEnvelopCallbackMessage {

    private long loanId;

    private long investId;

    private String loginName;

    private long userCouponId;

    public TransferRedEnvelopCallbackMessage(){}

    public TransferRedEnvelopCallbackMessage(long loanId, long investId, String loginName, long userCouponId) {
        this.loanId = loanId;
        this.investId = investId;
        this.loginName = loginName;
        this.userCouponId = userCouponId;
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

    public long getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(long userCouponId) {
        this.userCouponId = userCouponId;
    }
}
