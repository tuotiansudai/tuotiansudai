package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class WeChatHelpInfoModel implements Serializable {

    private long id;
    private long weChatUserInfoId;
    private long weChatHelpId;
    private Date createdTime;

    public WeChatHelpInfoModel() {
    }

    public WeChatHelpInfoModel(long weChatUserInfoId, long weChatHelpId) {
        this.weChatUserInfoId = weChatUserInfoId;
        this.weChatHelpId = weChatHelpId;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWeChatUserInfoId() {
        return weChatUserInfoId;
    }

    public void setWeChatUserInfoId(long weChatUserInfoId) {
        this.weChatUserInfoId = weChatUserInfoId;
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
