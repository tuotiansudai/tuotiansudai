package com.tuotiansudai.smswrapper.repository.model;


import java.util.Date;

public class JianZhouSmsHistoryModel{

    private long id;
    private String mobile;
    private String content;
    private boolean isVoice;
    private Date sendTime;
    private String response;

    public JianZhouSmsHistoryModel() {
    }

    public JianZhouSmsHistoryModel(String mobiles, String content, boolean isVoice) {
        this.mobile = mobile;
        this.content = content;
        this.isVoice = isVoice;
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

    public boolean isVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
