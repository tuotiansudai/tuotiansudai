package com.tuotiansudai.message;


public class UserInfo {

    private String loginName;
    private String mobile;
    private String userName;

    public UserInfo(){}

    public UserInfo(String loginName,String userName,String mobile){
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
