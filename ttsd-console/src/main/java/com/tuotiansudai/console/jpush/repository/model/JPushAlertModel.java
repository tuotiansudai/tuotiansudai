package com.tuotiansudai.console.jpush.repository.model;


import com.tuotiansudai.console.jpush.dto.JPushAlertDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class JPushAlertModel implements Serializable{

    private long id;
    private String name;
    private Date pushTime;
    private PushType pushType;
    private List<String> pushObjects;
    private PushSource pushSource;
    private PushStatus status;
    private String content;
    private JumpTo jumpTo;
    private String linkAddress;
    private Date createdTime;
    private String createdBy;
    private Date updatedTime;
    private String updatedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
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

    public PushStatus getStatus() {
        return status;
    }

    public void setStatus(PushStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JumpTo getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(JumpTo jumpTo) {
        this.jumpTo = jumpTo;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
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

    public List<String> getPushObjects() {
        return pushObjects;
    }

    public void setPushObjects(List<String> pushObjects) {
        this.pushObjects = pushObjects;
    }
    public JPushAlertModel(){

    }
    public JPushAlertModel(JPushAlertDto jPushAlertDto){
        this.name = jPushAlertDto.getName();
        this.pushTime = jPushAlertDto.getPushTime();
        this.pushType = jPushAlertDto.getPushType();
        this.pushObjects = jPushAlertDto.getPushObjects();
        this.pushSource = jPushAlertDto.getPushSource();
        this.status = PushStatus.CREATED;
        this.content = jPushAlertDto.getContent();
        this.jumpTo = jPushAlertDto.getJumpTo();
        this.linkAddress = jPushAlertDto.getLinkAddress();
        this.createdTime = new Date();
    }
}
