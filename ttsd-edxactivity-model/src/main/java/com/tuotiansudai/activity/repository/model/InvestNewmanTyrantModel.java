package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestNewmanTyrantModel implements Serializable {
    private long id;
    private long investId;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private boolean newman;
    private Date createdTime;

    public InvestNewmanTyrantModel(){}

    public InvestNewmanTyrantModel(long investId,
                                   String loginName,
                                   String userName,
                                   String mobile,
                                   long investAmount,
                                   boolean newman){
        this.investId = investId;
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.newman = newman;
        this.createdTime = new Date();
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isNewman() {
        return newman;
    }

    public void setNewman(boolean newman) {
        this.newman = newman;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
