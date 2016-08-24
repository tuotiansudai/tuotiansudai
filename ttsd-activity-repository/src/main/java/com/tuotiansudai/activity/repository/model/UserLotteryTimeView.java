package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;

public class UserLotteryTimeView implements Serializable{

    private String mobile;

    private String userName;

    private int useCount;

    private int unUseCount;

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

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getUnUseCount() {
        return unUseCount;
    }

    public void setUnUseCount(int unUseCount) {
        this.unUseCount = unUseCount;
    }
}
