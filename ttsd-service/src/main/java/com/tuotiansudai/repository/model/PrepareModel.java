package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class PrepareModel implements Serializable {
    private long id;
    private String referrerMobile;
    private String mobile;
    private PrepareChannel channel;
    private Date createdTime = new Date();

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

    public PrepareChannel getChannel() {
        return channel;
    }

    public void setChannel(PrepareChannel channel) {
        this.channel = channel;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
