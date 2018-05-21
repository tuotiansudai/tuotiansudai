package com.tuotiansudai.repository.model;


public class UserCouponExpiredView {

    private String mobile;

    private int expiredCount;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getExpiredCount() {
        return expiredCount;
    }

    public void setExpiredCount(int expiredCount) {
        this.expiredCount = expiredCount;
    }
}
