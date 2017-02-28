package com.tuotiansudai.message;

import java.io.Serializable;

public class CouponAssignSmsNotifyMessage implements Serializable {
    private long couponId;
    private String loginName;

    public CouponAssignSmsNotifyMessage() {
    }

    public CouponAssignSmsNotifyMessage(long couponId, String loginName) {
        this.couponId = couponId;
        this.loginName = loginName;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
