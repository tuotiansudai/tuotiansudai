package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AnnounceModel;

import java.io.Serializable;
import java.util.Date;

public class AnnounceDto extends BaseDataDto implements Serializable{

    private long id;

    private String title;

    private String content;

    private String contentText;

    private boolean showOnHome;

    private Date createdTime = new Date();

    private Date updateTime = new Date();

    public AnnounceDto() {
        super();
    }

    public AnnounceDto(AnnounceModel model) {
        this.id = model.getId();
        this.title = model.getTitle();
        this.content = model.getContent();
        this.showOnHome = model.isShowOnHome();
        this.createdTime = model.getCreatedTime();
        this.updateTime = model.getUpdateTime();
        this.contentText= model.getContentText();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShowOnHome() {
        return showOnHome;
    }

    public void setShowOnHome(boolean showOnHome) {
        this.showOnHome = showOnHome;
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

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}
