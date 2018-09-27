package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.CouponType;

public enum PreferenceType {

    INVEST_BENEFIT("出借奖励", null),
    RED_ENVELOPE("出借红包", CouponType.RED_ENVELOPE),
    NEWBIE_COUPON("新手体验金", CouponType.NEWBIE_COUPON),
    INVEST_COUPON("出借体验券", CouponType.INVEST_COUPON),
    INTEREST_COUPON("加息券", CouponType.INTEREST_COUPON),
    BIRTHDAY_COUPON("生日福利券", CouponType.BIRTHDAY_COUPON);

    final private String description;
    final private CouponType couponType;

    PreferenceType(String description, CouponType couponType) {
        this.description = description;
        this.couponType = couponType;
    }

    public String getDescription() {
        return description;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public CouponType convertToCouponType() {
        return couponType;
    }
}
