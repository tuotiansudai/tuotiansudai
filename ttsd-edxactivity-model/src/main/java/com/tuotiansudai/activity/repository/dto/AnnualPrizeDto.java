package com.tuotiansudai.activity.repository.dto;


import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;

public class AnnualPrizeDto {

    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private String rewards;

    public AnnualPrizeDto(AnnualPrizeModel annualPrizeModel, String rewards) {
        this.loginName = annualPrizeModel.getLoginName();
        this.userName = annualPrizeModel.getUserName();
        this.mobile = annualPrizeModel.getMobile();
        this.investAmount = annualPrizeModel.getInvestAmount();
        this.rewards = rewards;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }
}
