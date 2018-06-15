package com.tuotiansudai.repository.model;


import java.io.Serializable;

public class UserCouponExpiredView implements Serializable{

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
