package com.tuotiansudai.push.repository.model;


import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.push.dto.PushCreateDto;

import java.io.Serializable;
import java.util.Date;

public class PushAlertModel implements Serializable {
    private long id;
    private PushType pushType;
    private PushSource pushSource;
    private String content;
    private Date createdTime;
    private String createdBy;
    private Date updatedTime;
    private String updatedBy;

    public PushAlertModel() {
    }

    public PushAlertModel(String createdBy, PushCreateDto pushCreateDto) {
        this.pushType = pushCreateDto.getPushType();
        this.pushSource = pushCreateDto.getPushSource();
        this.content = pushCreateDto.getContent();
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
}
