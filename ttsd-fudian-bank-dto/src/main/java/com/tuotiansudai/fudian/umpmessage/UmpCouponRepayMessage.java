package com.tuotiansudai.fudian.umpmessage;

import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpCouponRepayMessage extends BankBaseMessage {

    private String loginName;

    private long couponRepayId;

    private long interest;

    private long fee;

    private boolean isNormalRepay;

    public UmpCouponRepayMessage() {
    }

    public UmpCouponRepayMessage(String loginName, long couponRepayId, long interest, long fee, boolean isNormalRepay) {
        this.loginName = loginName;
        this.couponRepayId = couponRepayId;
        this.interest = interest;
        this.fee = fee;
        this.isNormalRepay = isNormalRepay;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getCouponRepayId() {
        return couponRepayId;
    }

    public long getInterest() {
        return interest;
    }

    public long getFee() {
        return fee;
    }

    public boolean isNormalRepay() {
        return isNormalRepay;
    }
}
