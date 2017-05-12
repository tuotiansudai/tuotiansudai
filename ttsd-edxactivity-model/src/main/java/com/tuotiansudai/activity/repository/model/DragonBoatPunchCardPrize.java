package com.tuotiansudai.activity.repository.model;

/**
 * 端午节，微信打开奖品
 */
public enum DragonBoatPunchCardPrize {

    RedEnveloper5("5元红包优惠券", 410),
    RedEnveloper8("8元红包优惠券", 411),
    RedEnveloper10("10元红包优惠券", 412),
    RedEnveloper18("18元红包优惠券", 413),
    RedEnveloper28("28元红包优惠券", 414),
    InterestCoupon5("0.5%加息券", 415);

    String desc;

    long couponId;

    DragonBoatPunchCardPrize(String desc, long couponId) {
        this.desc = desc;
        this.couponId = couponId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }
}
