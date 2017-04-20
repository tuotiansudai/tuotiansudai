package com.tuotiansudai.message;


import java.util.Date;

public class UserInfoActivity extends UserInfo{

    private Date registerTime;

    public UserInfoActivity(){}

    public UserInfoActivity(UserInfo userInfo,Date registerTime){
        super(userInfo.getLoginName(),userInfo.getUserName(),userInfo.getMobile());
        this.registerTime = registerTime;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
