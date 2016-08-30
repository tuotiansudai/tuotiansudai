package com.tuotiansudai.point.repository.model;

import com.tuotiansudai.repository.model.CouponType;

public enum ItemType {
    PHYSICAL("实体商品", null),
    VIRTUAL("虚拟商品", null),
    RED_ENVELOPE("现金红包", CouponType.RED_ENVELOPE),
    INVEST_COUPON("投资体验券", CouponType.INVEST_COUPON),
    INTEREST_COUPON("加息券", CouponType.INTEREST_COUPON);

    final private String description;

    final private CouponType couponType;

    ItemType(String description, CouponType couponType) {
        this.description = description;
        this.couponType = couponType;
    }

    public String getDescription() {
        return description;
    }

    public CouponType convertToCouponType() {
        return couponType;
    }
}
