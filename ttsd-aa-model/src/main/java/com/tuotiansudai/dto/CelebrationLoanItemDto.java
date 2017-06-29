package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestAchievementView;

import java.util.List;

public class CelebrationLoanItemDto extends LoanItemDto{
    private List<InvestAchievementView> achievementViews;

    public List<InvestAchievementView> getAchievementViews() {
        return achievementViews;
    }

    public void setAchievementViews(List<InvestAchievementView> achievementViews) {
        this.achievementViews = achievementViews;
    }
}
