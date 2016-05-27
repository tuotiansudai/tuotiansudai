package com.tuotiansudai.message.dto;

import com.tuotiansudai.message.repository.model.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MessageDto implements Serializable{
    private long id;
    private String title;
    private String template;
    private MessageType type;
    private List<MessageUserGroup> userGroups;
    private List<MessageChannel> channels;
    private MessageStatus status;
    private long readCount;
    private String activatedBy;
    private Date activatedTime;
    private Date expiredTime;
    private String updatedBy;
    private Date updatedTime;
    private String createdBy;
    private Date createdTime;

    public MessageDto() {
    }

    public MessageDto(long id, String title, String template, MessageType type, List<MessageUserGroup> userGroups, List<MessageChannel> channels, MessageStatus status, long readCount, String activatedBy, Date activatedTime, Date expiredTime, String updatedBy, Date updatedTime, String createdBy, Date createdTime) {
        this.id = id;
        this.title = title;
        this.template = template;
        this.type = type;
        this.userGroups = userGroups;
        this.channels = channels;
        this.status = status;
        this.readCount = readCount;
        this.activatedBy = activatedBy;
        this.activatedTime = activatedTime;
        this.expiredTime = expiredTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
    }

    public MessageDto(MessageModel messageModel) {
        this.id = messageModel.getId();
        this.title = messageModel.getTitle();
        this.template = messageModel.getTemplate();
        this.type = messageModel.getType();
        this.userGroups = messageModel.getUserGroups();
        this.channels = messageModel.getChannels();
        this.status = messageModel.getStatus();
        this.readCount = messageModel.getReadCount();
        this.activatedBy = messageModel.getActivatedBy();
        this.activatedTime = messageModel.getActivatedTime();
        this.expiredTime = messageModel.getExpiredTime();
        this.updatedBy = messageModel.getUpdatedBy();
        this.updatedTime = messageModel.getUpdatedTime();
        this.createdBy = messageModel.getCreatedBy();
        this.createdTime = messageModel.getCreatedTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public List<MessageUserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<MessageUserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public List<MessageChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<MessageChannel> channels) {
        this.channels = channels;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public String getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(String activatedBy) {
        this.activatedBy = activatedBy;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
