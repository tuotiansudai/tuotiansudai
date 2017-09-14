package com.tuotiansudai.activity.repository.model;

public class SchoolSeasonView {

    private String loginName;
    private String userName;
    private String mobile;
    private long sumAmount;
    private int JDECard;


    public SchoolSeasonView() {
    }

    public SchoolSeasonView(ActivityInvestView activityInvestView, int JDECard) {
        this.loginName = activityInvestView.getLoginName();
        this.userName = activityInvestView.getUserName();
        this.mobile = activityInvestView.getMobile();
        this.sumAmount = activityInvestView.getSumAmount();
        this.JDECard = JDECard;
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

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
    }

    public int getJDECard() {
        return JDECard;
    }

    public void setJDECard(int JDECard) {
        this.JDECard = JDECard;
    }
}
