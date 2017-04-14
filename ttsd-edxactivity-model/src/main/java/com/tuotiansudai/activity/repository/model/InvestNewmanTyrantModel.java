package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestNewmanTyrantModel implements Serializable {
    private long id;
    private long invest_id;
    private String login_name;
    private String userName;
    private String mobile;
    private long investAmount;
    private boolean newman;
    private Date createdTime;

    public InvestNewmanTyrantModel(){}

    public InvestNewmanTyrantModel(long invest_id,
                                   String login_name,
                                   String userName,
                                   String mobile,
                                   long investAmount,
                                   boolean newman){
        this.invest_id = invest_id;
        this.login_name = login_name;
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

    public long getInvest_id() {
        return invest_id;
    }

    public void setInvest_id(long invest_id) {
        this.invest_id = invest_id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
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
}
