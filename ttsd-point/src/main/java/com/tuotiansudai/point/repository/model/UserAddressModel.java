package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserAddressModel implements Serializable{

    private long id;
    private String loginName;
    private String realName;
    private String mobile;
    private String address;
    private String createdBy;
    private Date createdTime;

    public UserAddressModel(){

    }

    public UserAddressModel(String loginName, String realName, String mobile, String address, String createdBy) {
        this.loginName = loginName;
        this.realName = realName;
        this.mobile = mobile;
        this.address = address;
        this.createdBy = createdBy;
        this.createdTime = new Date();
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
