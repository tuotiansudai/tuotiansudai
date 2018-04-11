package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class WeChatHelpInfoModel implements Serializable {

    private long id;
    private String openId;
    private long weChatHelpId;
    private Date createdTime;

    public WeChatHelpInfoModel() {
    }

    public WeChatHelpInfoModel(String openId, long weChatHelpId) {
        this.openId = openId;
        this.weChatHelpId = weChatHelpId;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getWeChatHelpId() {
        return weChatHelpId;
    }

    public void setWeChatHelpId(long weChatHelpId) {
        this.weChatHelpId = weChatHelpId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
