package com.tuotiansudai.fudian.umpdto;


import com.google.common.base.Strings;

public class UmpCouponRepayDto extends UmpBaseDto{

    private long couponModelId;

    private long amount;

    private String payAccountId;

    public UmpCouponRepayDto() {
    }

    public UmpCouponRepayDto(String loginName, String payUserId, long couponModelId, long amount, String payAccountId) {
        super(loginName, payUserId);
        this.couponModelId = couponModelId;
        this.amount = amount;
        this.payAccountId = payAccountId;
    }

    public long getCouponModelId() {
        return couponModelId;
    }

    public long getAmount() {
        return amount;
    }

    public String getPayAccountId() {
        return payAccountId;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && couponModelId > 0
                && amount > 0
                && !Strings.isNullOrEmpty(payAccountId);
    }

}
