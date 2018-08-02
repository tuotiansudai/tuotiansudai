package com.tuotiansudai.activity.repository.model;


import java.util.Date;
import java.util.List;

public class ThirdAnniversaryHelpView extends ThirdAnniversaryHelpModel{

    private String investAmount;
    private String annualizedAmount;
    private int helpCount;
    private String rate;
    private String reward;
    private String friends;

    public ThirdAnniversaryHelpView() {
    }

    public ThirdAnniversaryHelpView(ThirdAnniversaryHelpModel model, String investAmount, String annualizedAmount, int helpCount, String rate, String reward, String friends) {
        super(model.getLoginName(), model.getMobile(), model.getUserName(), model.getStartTime(), model.getEndTime());
        this.investAmount = investAmount;
        this.annualizedAmount = annualizedAmount;
        this.helpCount = helpCount;
        this.rate = rate;
        this.reward = reward;
        this.friends = friends;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getAnnualizedAmount() {
        return annualizedAmount;
    }

    public void setAnnualizedAmount(String annualizedAmount) {
        this.annualizedAmount = annualizedAmount;
    }

    public int getHelpCount() {
        return helpCount;
    }

    public void setHelpCount(int helpCount) {
        this.helpCount = helpCount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }
}
