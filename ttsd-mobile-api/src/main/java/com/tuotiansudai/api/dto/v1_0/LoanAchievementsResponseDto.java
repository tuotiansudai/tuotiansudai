package com.tuotiansudai.api.dto.v1_0;


public class LoanAchievementsResponseDto extends BaseResponseDataDto{

    private String achievement;
    private String mobile;
    private String coupon;

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}
