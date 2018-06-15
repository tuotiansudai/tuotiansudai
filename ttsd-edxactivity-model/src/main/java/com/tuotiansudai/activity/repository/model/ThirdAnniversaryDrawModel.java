package com.tuotiansudai.activity.repository.model;


import java.util.Date;

public class ThirdAnniversaryDrawModel {

    private String loginName;
    private String teamName;
    private int teamCount;
    private Date createdTime;

    public ThirdAnniversaryDrawModel() {
    }

    public ThirdAnniversaryDrawModel(String loginName, String teamName) {
        this.loginName = loginName;
        this.teamName = teamName;
        this.teamCount = 1;
    }

    public ThirdAnniversaryDrawModel(String loginName, String teamName, int teamCount) {
        this.loginName = loginName;
        this.teamName = teamName;
        this.teamCount = teamCount;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
