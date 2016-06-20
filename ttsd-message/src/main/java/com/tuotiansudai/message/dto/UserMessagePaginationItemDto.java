package com.tuotiansudai.message.dto;

import com.tuotiansudai.message.repository.model.UserMessageModel;

import java.util.Date;

public class UserMessagePaginationItemDto {

    private long userMessageId;

    private String title;

    private String content;

    private boolean read;

    private Date createdTime;

    public UserMessagePaginationItemDto(UserMessageModel model) {
        this.userMessageId = model.getMessageId();
        this.title = model.getTitle();
        this.content = model.getContent();
        this.read = model.isRead();
        this.createdTime = model.getCreatedTime();
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return read;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
