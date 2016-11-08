package com.tuotiansudai.cfca.model;

import java.io.Serializable;
import java.util.Date;

public class AnxinSendCaptchaRequestModel implements Serializable {

    private long id;

    private String txTime;

    private String userId;

    private String projectCode;

    private String isSendVoice;

    private Date createdTime;


    public AnxinSendCaptchaRequestModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getIsSendVoice() {
        return isSendVoice;
    }

    public void setIsSendVoice(String isSendVoice) {
        this.isSendVoice = isSendVoice;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
