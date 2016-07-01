package com.tuotiansudai.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserMessagePaginationItemDto {

    private long userMessageId;

    private String title;

    private String content;

    private boolean read;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;

    public UserMessagePaginationItemDto(UserMessageModel model) {
        this.userMessageId = model.getId();
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

    public void setRead(boolean read) {
        this.read = read;
    }
}
