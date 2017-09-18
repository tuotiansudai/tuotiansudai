package com.tuotiansudai.smswrapper.repository.model;


import com.tuotiansudai.smswrapper.SmsChannel;

import java.io.Serializable;
import java.util.Date;

public class SmsHistoryModel implements Serializable {

    private long id;

    private String mobile;

    private String content;

    private SmsChannel channel;

    private Date sendTime;

    private boolean success;

    private String response;

    private Long backupId;

    public SmsHistoryModel() {
    }

    public SmsHistoryModel(String mobile, String content, SmsChannel channel) {
        this.mobile = mobile;
        this.content = content;
        this.channel = channel;
        this.sendTime = new Date();
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SmsChannel getChannel() {
        return channel;
    }

    public void setChannel(SmsChannel channel) {
        this.channel = channel;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getBackupId() {
        return backupId;
    }

    public void setBackupId(Long backupId) {
        this.backupId = backupId;
    }
}
