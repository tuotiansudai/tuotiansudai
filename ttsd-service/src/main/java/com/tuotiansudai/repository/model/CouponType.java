package com.tuotiansudai.repository.model;

public enum CouponType {

    NEWBIE_COUPON("新手体验券", "新手"),
    INVEST_COUPON("投资体验券", "投资");

    private String name;

    private String abbr;

    CouponType(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
    }

    public static boolean isNewBieCoupon(CouponType couponType){
        return couponType.equals(CouponType.NEWBIE_COUPON);
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }
}
