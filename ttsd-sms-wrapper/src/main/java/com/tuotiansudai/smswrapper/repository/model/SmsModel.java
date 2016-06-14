package com.tuotiansudai.smswrapper.repository.model;


import java.util.Date;

public class SmsModel {

    private long id;

    private String mobile;

    private String content;

    private Date sendTime = new Date();

    private String resultCode;

    public SmsModel() {
    }

    public SmsModel(String mobile, String content, String resultCode) {
        this.mobile = mobile;
        this.content = content;
        this.resultCode = resultCode;
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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmsModel that = (SmsModel) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
