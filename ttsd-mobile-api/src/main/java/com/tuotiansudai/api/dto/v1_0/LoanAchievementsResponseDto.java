package com.tuotiansudai.api.dto.v1_0;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.repository.model.InvestAchievement;

import java.util.List;

public class LoanAchievementsResponseDto extends BaseResponseDataDto{

    private String achievement;
    private String mobile;
    private List<String> coupon;

    public LoanAchievementsResponseDto(UserGroup userGroup) {
        switch (userGroup){
            case FIRST_INVEST_ACHIEVEMENT:
                this.achievement = InvestAchievement.FIRST_INVEST.name();
                break;
            case MAX_AMOUNT_ACHIEVEMENT:
                this.achievement = InvestAchievement.MAX_AMOUNT.name();
                break;
            case LAST_INVEST_ACHIEVEMENT:
                this.achievement = InvestAchievement.LAST_INVEST.name();
                break;
        }
        this.coupon = Lists.newArrayList();
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

    public List<String> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<String> coupon) {
        this.coupon = coupon;
    }
}
