package com.tuotiansudai.api.dto.v1_0;


import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.repository.model.InvestAchievement;

import java.util.List;

public class LoanAchievementsResponseDto extends BaseResponseDataDto{

    private InvestAchievement achievement;
    private String mobile;
    private List<String> coupon;

    public LoanAchievementsResponseDto(UserGroup userGroup) {
        switch (userGroup){
            case FIRST_INVEST_ACHIEVEMENT:
                this.achievement = InvestAchievement.FIRST_INVEST;
                break;
            case MAX_AMOUNT_ACHIEVEMENT:
                this.achievement = InvestAchievement.MAX_AMOUNT;
                break;
            case LAST_INVEST_ACHIEVEMENT:
                this.achievement = InvestAchievement.LAST_INVEST;
                break;
        }
        this.coupon = Lists.newArrayList();
    }

    public InvestAchievement getAchievement() {
        return achievement;
    }

    public void setAchievement(InvestAchievement achievement) {
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
