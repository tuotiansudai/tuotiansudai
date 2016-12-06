package com.tuotiansudai.message.dto;

import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.push.repository.model.PushAlertModel;

import java.util.Date;
import java.util.List;

public class MessagePaginationItemDto {

    private long id;

    private String title;

    private String templateTxt;

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

    public MessagePaginationItemDto(MessageModel messageModel, PushAlertModel pushAlertModel) {
        this.id = messageModel.getId();
        this.title = messageModel.getTitle();
        this.templateTxt = messageModel.getTemplateTxt();
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
        this.push = pushAlertModel != null ? new PushPaginationItemDto(pushAlertModel) : null;
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
