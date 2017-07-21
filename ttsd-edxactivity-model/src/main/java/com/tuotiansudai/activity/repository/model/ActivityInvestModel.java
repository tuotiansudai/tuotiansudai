package com.tuotiansudai.activity.repository.model;


import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class ActivityInvestModel implements Serializable{

    private long id;
    private long investId;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private String activityName;
    private Date createTime;

    public ActivityInvestModel() {
    }

    public ActivityInvestModel(long investId, String loginName, String userName, String mobile, long investAmount, String activityName) {
        this.investId = investId;
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.activityName = activityName;
        this.createTime = new Date();
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
