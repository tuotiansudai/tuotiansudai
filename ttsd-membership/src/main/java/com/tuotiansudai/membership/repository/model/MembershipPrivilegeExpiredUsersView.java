package com.tuotiansudai.membership.repository.model;


import java.io.Serializable;

public class MembershipPrivilegeExpiredUsersView implements Serializable{

    private String loginName;

    private String mobile;

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
}
