package com.tuotiansudai.message.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserMessageModel implements Serializable {
    private long id;
    private long messageId;
    private String loginName;
    private String title;
    private String content;
    private boolean read;
    private Date readTime;
    private Date createdTime;

    public UserMessageModel() {
    }

    public UserMessageModel(long messageId, String loginName, String title, String content) {
        this.messageId = messageId;
        this.loginName = loginName;
        this.title = title;
        this.content = content;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
