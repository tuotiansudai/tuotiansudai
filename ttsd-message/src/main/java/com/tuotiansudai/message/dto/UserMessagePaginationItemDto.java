package com.tuotiansudai.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.message.repository.model.MessageCategory;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.repository.model.UserMessageModel;

import java.util.Date;

public class UserMessagePaginationItemDto {

    private long userMessageId;

    private String title;

    private String content;

    private boolean read;

    private MessageType messageType;

    private MessageCategory messageCategory;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdTime;

    public UserMessagePaginationItemDto(UserMessageModel model, MessageModel messageModel) {
        this.userMessageId = model.getId();
        this.title = model.getTitle();
        this.content = model.getContent();
        this.read = model.isRead();
        this.messageType = messageModel.getType();
        this.messageCategory = messageModel.getMessageCategory();
        if (messageModel.getType().equals(MessageType.MANUAL)) {
            this.createdTime = messageModel.getActivatedTime();
        } else {
            this.createdTime = model.getCreatedTime();
        }
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageCategory getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(MessageCategory messageCategory) {
        this.messageCategory = messageCategory;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
