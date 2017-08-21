package com.tuotiansudai.activity.repository.model;

import java.util.Date;

public class SchoolExclusiveModel {

    private long id;
    private long loanId;
    private String loginName;
    private long sumAmount;
    private boolean topThree;
    private Date createdTime;

    public SchoolExclusiveModel() {
    }

    public SchoolExclusiveModel(long loanId, String loginName, long sumAmount, boolean topThree, Date createdTime) {
        this.loanId = loanId;
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

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
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
