package com.tuotiansudai.message.dto;

import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.message.repository.model.*;

import java.util.Date;
import java.util.List;

public class MessagePaginationItemDto {

    private long id;

    private String title;

    private String templateTxt;

    private Date validStartTime;

    private Date validEndTime;

    private MessageEventType messageEventType;

    private MessageUserGroup userGroup;

    private List<MessageChannel> channels;

    private MessageCategory messageCategory;

    private String webUrl;

    private AppUrl appUrl;

    private MessageStatus messageStatus;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String activatedBy;

    private Date activatedTime;

    private PushPaginationItemDto push;

    public MessagePaginationItemDto(MessageModel messageModel, PushModel pushModel) {
        this.id = messageModel.getId();
        this.title = messageModel.getTitle();
        this.templateTxt = messageModel.getTemplateTxt();
        this.validStartTime = messageModel.getValidStartTime();
        this.validEndTime = messageModel.getValidEndTime();
        this.messageEventType = messageModel.getEventType();
        this.userGroup = messageModel.getUserGroup();
        this.channels = messageModel.getChannels();
        this.messageCategory = messageModel.getMessageCategory();
        this.webUrl = messageModel.getWebUrl();
        this.appUrl = messageModel.getAppUrl();
        this.messageStatus = messageModel.getStatus();
        this.createdBy = messageModel.getCreatedBy();
        this.createdTime = messageModel.getCreatedTime();
        this.updatedBy = messageModel.getUpdatedBy();
        this.updatedTime = messageModel.getUpdatedTime();
        this.activatedBy = messageModel.getActivatedBy();
        this.activatedTime = messageModel.getActivatedTime();
        this.push = pushModel != null ? new PushPaginationItemDto(pushModel) : null;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTemplateTxt() {
        return templateTxt;
    }

    public Date getValidStartTime() {
        return validStartTime;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public MessageEventType getMessageEventType() {
        return messageEventType;
    }

    public MessageUserGroup getUserGroup() {
        return userGroup;
    }

    public List<MessageChannel> getChannels() {
        return channels;
    }

    public MessageCategory getMessageCategory() {
        return messageCategory;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public AppUrl getAppUrl() {
        return appUrl;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public String getActivatedBy() {
        return activatedBy;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public PushPaginationItemDto getPush() {
        return push;
    }
}
