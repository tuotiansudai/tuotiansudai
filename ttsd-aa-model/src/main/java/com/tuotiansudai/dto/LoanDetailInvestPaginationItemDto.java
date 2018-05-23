package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public class LoanDetailInvestPaginationItemDto {

    private String loginName;

    private String amount;

    private Source source;

    private String expectedInterest;

    private Date createdTime;

    private String mobile;

    private List<InvestAchievement> achievements;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<InvestAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<InvestAchievement> achievements) {
        this.achievements = achievements;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
