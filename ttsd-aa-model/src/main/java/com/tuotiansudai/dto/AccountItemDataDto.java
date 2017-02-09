package com.tuotiansudai.dto;

import java.io.Serializable;

public class AccountItemDataDto implements Serializable {
    private String loginName;
    private String userName;
    private String mobile;
    private long point;
    private long totalPoint;

    public AccountItemDataDto(String loginName, String userName, long point) {
        this.loginName = loginName;
        this.userName = userName;
        this.point = point;
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

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(long totalPoint) {
        this.totalPoint = totalPoint;
    }
}

