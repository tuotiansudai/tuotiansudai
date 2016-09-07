package com.tuotiansudai.console.activity.dto;

import java.util.Date;
import java.util.List;

public class UserItemExportDto {

    private String loginName;

    private String mobile;

    private Date joinTime;

    private long investAmount;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    @Override
    public String toString() {
        return "UserItemExportDto{" +
                "loginName='" + loginName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", joinTime=" + joinTime +
                ", investAmount='" + investAmount + '\'' +
                '}';
    }
}
