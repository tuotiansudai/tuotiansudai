package com.tuotiansudai.activity.repository.model;

import org.joda.time.DateTime;

public class SchoolExclusiveModel {

    private long id;
    private long investId;
    private String loginName;
    private long sumAmount;
    private boolean topThree;
    private DateTime createdTime;

    public SchoolExclusiveModel() {
    }

    public SchoolExclusiveModel(long investId, String loginName, long sumAmount, boolean topThree, DateTime createdTime) {
        this.investId = investId;
        this.loginName = loginName;
        this.sumAmount = sumAmount;
        this.topThree = topThree;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public DateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(DateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
    }

    public boolean isTopThree() {
        return topThree;
    }

    public void setTopThree(boolean topThree) {
        this.topThree = topThree;
    }
}
