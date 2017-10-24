package com.tuotiansudai.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.enums.AppUrl;

import java.util.Date;

public class UserMessageViewDto extends BaseResponseDataDto {
    private long userMessageId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdTime;
    private String appUrl;

    public UserMessageViewDto() {
    }

    public UserMessageViewDto(long userMessageId, String title, String content, Date createdTime, String appUrl) {
        this.userMessageId = userMessageId;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.appUrl = appUrl;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
