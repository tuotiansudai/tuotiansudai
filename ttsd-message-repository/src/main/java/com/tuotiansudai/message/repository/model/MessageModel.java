package com.tuotiansudai.message.repository.model;

import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.MessageType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MessageModel implements Serializable {
    private long id;
    private String title;
    private String template;
    private String templateTxt;
    private MessageType type;
    private MessageEventType eventType;
    private MessageUserGroup userGroup;
    private List<MessageChannel> channels;
    private MessageCategory messageCategory;
    private String webUrl;
    private AppUrl appUrl;
    private MessageStatus status;
    private long readCount;
    private Long pushId;
    private String activatedBy;
    private Date activatedTime;
    private Date validStartTime;
    private Date validEndTime;
    private String updatedBy;
    private Date updatedTime;
    private String createdBy;
    private Date createdTime;

    public MessageModel() {
    }

    public MessageModel(String title, String template, String templateTxt, MessageUserGroup userGroup, MessageCategory messageCategory, List<MessageChannel> channels, String webUrl, AppUrl appUrl, Long pushId, String createdBy, Date validStartTime, Date validEndTime) {
        this.title = title;
        this.template = template;
        this.templateTxt = templateTxt;
        this.type = MessageType.MANUAL;
        this.messageCategory = messageCategory;
        this.userGroup = userGroup;
        this.channels = channels;
        this.status = MessageStatus.TO_APPROVE;
        this.pushId = pushId;
        this.webUrl = webUrl;
        this.appUrl = appUrl;
        this.createdBy = createdBy;
        this.createdTime = new Date();
        this.updatedBy = this.createdBy;
        this.updatedTime = this.createdTime;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
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

    public String getTemplateTxt() {
        return templateTxt;
    }

    public void setTemplateTxt(String templateTxt) {
        this.templateTxt = templateTxt;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageEventType getEventType() {
        return eventType;
    }

    public void setEventType(MessageEventType eventType) {
        this.eventType = eventType;
    }

    public MessageUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(MessageUserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public List<MessageChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<MessageChannel> channels) {
        this.channels = channels;
    }

    public MessageCategory getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(MessageCategory messageCategory) {
        this.messageCategory = messageCategory;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public AppUrl getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(AppUrl appUrl) {
        this.appUrl = appUrl;
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

    public Long getPushId() {
        return pushId;
    }

    public void setPushId(Long pushId) {
        this.pushId = pushId;
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

    public Date getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(Date validStartTime) {
        this.validStartTime = validStartTime;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
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
