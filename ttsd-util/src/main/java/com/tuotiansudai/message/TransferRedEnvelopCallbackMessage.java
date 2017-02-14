package com.tuotiansudai.message;


public class TransferRedEnvelopCallbackMessage {

    private Long loanId;

    private Long investId;

    private String loginName;

    private Long userCouponId;

    public TransferRedEnvelopCallbackMessage(){}

    public TransferRedEnvelopCallbackMessage(Long loanId, Long investId, String loginName, Long userCouponId) {
        this.loanId = loanId;
        this.investId = investId;
        this.loginName = loginName;
        this.userCouponId = userCouponId;
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

    public Long getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(Long userCouponId) {
        this.userCouponId = userCouponId;
    }
}
