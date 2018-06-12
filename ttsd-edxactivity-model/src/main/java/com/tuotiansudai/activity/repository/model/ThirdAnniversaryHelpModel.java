package com.tuotiansudai.activity.repository.model;


import java.util.Date;

public class ThirdAnniversaryHelpModel {

    private long id;
    private String loginName;
    private String mobile;
    private String userName;
    private Date startTime;
    private Date endTime;

    public ThirdAnniversaryHelpModel() {
    }

    public ThirdAnniversaryHelpModel(String loginName, String mobile, String userName, Date startTime, Date endTime) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
