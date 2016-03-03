package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AccountItemDataDto implements Serializable {
    private String loginName;
    private String userName;
    private String mobile;
    private long point;
    private long totalPoint;

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

    public AccountItemDataDto(AccountModel accountModel) {
        this.loginName = accountModel.getLoginName();
        this.userName = accountModel.getUserName();
        this.point = accountModel.getPoint();
    }
}

