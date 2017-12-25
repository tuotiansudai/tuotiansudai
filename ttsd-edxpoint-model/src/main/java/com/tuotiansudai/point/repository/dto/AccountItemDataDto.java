package com.tuotiansudai.point.repository.dto;

import java.io.Serializable;

public class AccountItemDataDto implements Serializable {
    private final String loginName;
    private final String userName;
    private final String mobile;
    private final long point;
    private final long totalPoint;

    public AccountItemDataDto(String loginName, String userName, String mobile, long point, long totalPoint) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.point = point;
        this.totalPoint = totalPoint;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public long getPoint() {
        return point;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

}

