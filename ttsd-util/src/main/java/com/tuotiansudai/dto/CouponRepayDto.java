package com.tuotiansudai.dto;


public class CouponRepayDto {
    private long loanRepayId;
    private String payUserId;
    private long couponRepayId;
    private long userCouponId;
    private long transferAmount;

    public CouponRepayDto(){

    }

    public CouponRepayDto(long loanRepayId, String payUserId, long couponRepayId, long userCouponId, long transferAmount) {
        this.loanRepayId = loanRepayId;
        this.payUserId = payUserId;
        this.couponRepayId = couponRepayId;
        this.userCouponId = userCouponId;
        this.transferAmount = transferAmount;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
    }

    public long getCouponRepayId() {
        return couponRepayId;
    }

    public void setCouponRepayId(long couponRepayId) {
        this.couponRepayId = couponRepayId;
    }

    public long getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(long userCouponId) {
        this.userCouponId = userCouponId;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(long transferAmount) {
        this.transferAmount = transferAmount;
    }
}
