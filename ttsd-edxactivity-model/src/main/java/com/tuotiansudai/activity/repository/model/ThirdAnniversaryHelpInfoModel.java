package com.tuotiansudai.activity.repository.model;


import java.util.Date;

public class ThirdAnniversaryHelpInfoModel {

    private long id;
    private long helpId;
    private String loginName;
    private String mobile;
    private String userName;
    private Date createdTime;

    public ThirdAnniversaryHelpInfoModel() {
    }

    public ThirdAnniversaryHelpInfoModel(long helpId, String loginName, String mobile, String userName) {
        this.helpId = helpId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHelpId() {
        return helpId;
    }

    public void setHelpId(long helpId) {
        this.helpId = helpId;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
