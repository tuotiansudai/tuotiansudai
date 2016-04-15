package com.tuotiansudai.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LinkExchangeDto implements Serializable{

    private long id;

    private String title;

    private String linkUrl;

    private Date createdTime = new Date();

    private Date updateTime = new Date();

    private SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LinkExchangeDto() {}

    public LinkExchangeDto(long id, String title, String linkUrl, Date createdTime, Date updateTime) {
        this.id = id;
        this.title = title;
        this.linkUrl = linkUrl;
        this.createdTime = createdTime;
        this.updateTime = updateTime;
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String convertToString() {
        return id + "|" + title.trim() + "|" + linkUrl.trim() + "|" + dfs.format(updateTime) + "|" + dfs.format(createdTime);
    }
}
