package com.tuotiansudai.fudian.umpdto;


public class UmpCouponRepayDto extends UmpBaseDto {

    private long couponRepayId;

    private long interest;

    private long fee;

    private boolean isNormalRepay;

    public UmpCouponRepayDto() {
    }

    public UmpCouponRepayDto(String loginName, String payUserId, long couponRepayId, long interest, long fee, boolean isNormalRepay) {
        super(loginName, payUserId);
        this.couponRepayId = couponRepayId;
        this.interest = interest;
        this.fee = fee;
        this.isNormalRepay = isNormalRepay;
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

    public boolean getIsNormalRepay() {
        return isNormalRepay;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && couponRepayId > 0
                && interest > 0
                && fee >= 0;
    }
}
