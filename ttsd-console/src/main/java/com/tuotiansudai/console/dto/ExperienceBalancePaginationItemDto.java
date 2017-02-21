package com.tuotiansudai.console.dto;


import com.tuotiansudai.repository.model.UserModel;

import java.io.Serializable;
import java.util.Date;

public class ExperienceBalancePaginationItemDto implements Serializable{
    private String loginName;
    private String userName;
    private String mobile;
    private String province;
    private Date lastExchangeTime;
    private long experienceBalance;

    public ExperienceBalancePaginationItemDto(){

    }
    public ExperienceBalancePaginationItemDto(UserModel userModel, Date lastExchangeTime){
        this.loginName = userModel.getLoginName();
        this.userName = userModel.getUserName();
        this.mobile = userModel.getMobile();
        this.province = userModel.getProvince();
        this.lastExchangeTime = lastExchangeTime;
        this.experienceBalance = userModel.getExperienceBalance();
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Date getLastExchangeTime() {
        return lastExchangeTime;
    }

    public void setLastExchangeTime(Date lastExchangeTime) {
        this.lastExchangeTime = lastExchangeTime;
    }

    public long getExperienceBalance() {
        return experienceBalance;
    }

    public void setExperienceBalance(long experienceBalance) {
        this.experienceBalance = experienceBalance;
    }
}
