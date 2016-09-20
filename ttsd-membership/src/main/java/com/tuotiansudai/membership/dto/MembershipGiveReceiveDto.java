package com.tuotiansudai.membership.dto;

import java.util.Date;

public class MembershipGiveReceiveDto {
    private String loginName;
    private String mobile;
    private Date receiveTime;

    public MembershipGiveReceiveDto() {
    }

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

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }
}
