package com.tuotiansudai.message.repository.model;


import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;

import java.io.Serializable;
import java.util.Date;

public class PushModel implements Serializable {
    private long id;
    private PushType pushType;
    private PushSource pushSource;
    private String content;
    private AppUrl jumpTo;
    private Date createdTime;
    private String createdBy;
    private Date updatedTime;
    private String updatedBy;

    public PushModel() {
    }

    public PushModel(String createdBy, PushType pushType, PushSource pushSource, String content, AppUrl jumpTo) {
        this.pushType = pushType;
        this.pushSource = pushSource;
        this.content = content;
        this.jumpTo = jumpTo;
        this.createdBy = createdBy;
        this.createdTime = new Date();
        this.updatedBy = createdBy;
        this.updatedTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PushType getPushType() {
        return pushType;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }

    public PushSource getPushSource() {
        return pushSource;
    }

    public void setPushSource(PushSource pushSource) {
        this.pushSource = pushSource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public AppUrl getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(AppUrl jumpTo) {
        this.jumpTo = jumpTo;
    }
}
