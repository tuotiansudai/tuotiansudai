package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;

import java.io.Serializable;

public class AccountItemDataDto implements Serializable {
    private String loginName;
    private String userName;
    private String mobile;
    private long point;
    private long totalPoint;

    public AccountItemDataDto(UserModel userModel, AccountModel accountModel) {
        this.loginName = accountModel.getLoginName();
        this.userName = userModel.getUserName();
        this.point = accountModel.getPoint();
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

