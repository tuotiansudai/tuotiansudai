package com.tuotiansudai.api.dto.v1_0;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class UserMessageDto implements Serializable {
    private long userMessageId;
    private String title;
    private String content;
    private boolean read;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdTime;

    public UserMessageDto(UserMessageModel userMessageModel) {
        this.userMessageId = userMessageModel.getId();
        this.title = userMessageModel.getTitle();
        this.content = StringUtils.isEmpty(userMessageModel.getContent()) ? userMessageModel.getTitle() : userMessageModel.getContent();
        this.read = userMessageModel.isRead();
        this.createdTime = userMessageModel.getCreatedTime();
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}



