package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryTimeModel implements Serializable {

    private long id;

    private String mobile;

    private String loginName;

    private int useCount;

    private int unUseCount;

    private Date createdTime;

    private Date updatedTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
