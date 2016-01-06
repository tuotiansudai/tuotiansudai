package com.tuotiansudai.repository.model;

public enum CouponType {

    NEWBIE_COUPON("新手体验券"),
    INVEST_COUPON("投资体验券");

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    CouponType(String desc) {
        this.desc = desc;
    }

    public static boolean isNewBieCoupon(CouponType couponType){
        return couponType.equals(CouponType.NEWBIE_COUPON);
    }
}
