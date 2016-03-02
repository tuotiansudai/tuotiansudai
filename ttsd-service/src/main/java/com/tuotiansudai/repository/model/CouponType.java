package com.tuotiansudai.repository.model;

public enum CouponType {

    RED_ENVELOPE("现金红包", "红包", 1),
    NEWBIE_COUPON("新手体验券", "新手", 2),
    INVEST_COUPON("投资体验券", "投资", 3),
    INTEREST_COUPON("加息券", "加息", 4),
    BIRTHDAY_COUPON("生日福利", "生日", 5);

    private String name;

    private String abbr;

    private int order;

    CouponType(String name, String abbr, int order) {
        this.name = name;
        this.abbr = abbr;
        this.order = order;
    }

    public static boolean isNewBieCoupon(CouponType couponType) {
        return couponType.equals(CouponType.NEWBIE_COUPON);
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public UserBillBusinessType getUserBillBusinessType() {
        switch (this) {
            case NEWBIE_COUPON:
                return UserBillBusinessType.NEWBIE_COUPON;
            case INVEST_COUPON:
                return UserBillBusinessType.INVEST_COUPON;
            case INTEREST_COUPON:
                return UserBillBusinessType.INTEREST_COUPON;
            case RED_ENVELOPE:
                return UserBillBusinessType.RED_ENVELOPE;
            case BIRTHDAY_COUPON:
                return UserBillBusinessType.BIRTHDAY_COUPON;
        }

        return null;
    }
}
