package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.repository.model.InvestAchievement;

public class LoanAchievementsResponseDto extends BaseResponseDataDto{

    private String achievement;
    private String mobile;
    private String coupon;

    public LoanAchievementsResponseDto(UserGroup userGroup) {
        switch (userGroup){
            case FIRST_INVEST_ACHIEVEMENT:
                this.achievement = InvestAchievement.FIRST_INVEST.name();
                break;
            case MAX_AMOUNT_ACHIEVEMENT:
                this.achievement = InvestAchievement.LAST_INVEST.name();
                break;
            case LAST_INVEST_ACHIEVEMENT:
                this.achievement = InvestAchievement.MAX_AMOUNT.name();
                break;
        }
        this.coupon = "";
    }

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
