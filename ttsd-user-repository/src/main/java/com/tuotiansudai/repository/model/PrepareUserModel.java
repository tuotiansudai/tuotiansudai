package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class PrepareUserModel implements Serializable {
    private long id;
    private String referrerMobile;
    private String mobile;
    private Source channel;
    private Date createdTime;
    private String referrerLoginName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Source getChannel() {
        return channel;
    }

    public void setChannel(Source channel) {
        this.channel = channel;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }
}
