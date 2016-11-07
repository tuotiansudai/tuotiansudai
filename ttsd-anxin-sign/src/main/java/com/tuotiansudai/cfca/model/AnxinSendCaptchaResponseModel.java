package com.tuotiansudai.cfca.model;

import java.io.Serializable;
import java.util.Date;

public class AnxinSendCaptchaResponseModel implements Serializable {

    private long id;

    private long txTime;

    private String retCode;

    private String retMessage;

    private long userId;

    private String projectCode;

    private long isSendVoice;

    private Date createdTime;


    public AnxinSendCaptchaResponseModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTxTime() {
        return txTime;
    }

    public void setTxTime(long txTime) {
        this.txTime = txTime;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public long getIsSendVoice() {
        return isSendVoice;
    }

    public void setIsSendVoice(long isSendVoice) {
        this.isSendVoice = isSendVoice;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
