package com.tuotiansudai.smswrapper.repository.model;


import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

public class NeteaseCallbackRequestModel implements Serializable {

    private long id;

    private long smsHistoryId;

    private String mobile;

    private String sendid;

    private String result;

    private Date sendTime;

    private Date reportTime;

    private Integer spliced;

    private Date callbackTime;

    private Date createdTime;

    public NeteaseCallbackRequestModel() {
    }

    public NeteaseCallbackRequestModel(long smsHistoryId, String mobile, String sendid) {
        this.smsHistoryId = smsHistoryId;
        this.mobile = mobile;
        this.sendid = sendid;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSmsHistoryId() {
        return smsHistoryId;
    }

    public void setSmsHistoryId(long smsHistoryId) {
        this.smsHistoryId = smsHistoryId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getSpliced() {
        return spliced;
    }

    public void setSpliced(Integer spliced) {
        this.spliced = spliced;
    }

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return MessageFormat.format("NeteaseCallbackRequestModel: " +
                        "id={0}, " +
                        "smsHistoryId={1}, " +
                        "mobile={2}, " +
                        "sendid={3}, " +
                        "result={4}, " +
                        "sendTime={5}, " +
                        "reportTime={6}, " +
                        "spliced={7}, " +
                        "callbackTime={8}, " +
                        "createdTime={9}",
                String.valueOf(this.id),
                String.valueOf(this.smsHistoryId),
                this.mobile,
                this.sendid,
                this.result,
                this.sendTime,
                this.reportTime,
                String.valueOf(this.spliced),
                this.callbackTime,
                this.createdTime);
    }
}
