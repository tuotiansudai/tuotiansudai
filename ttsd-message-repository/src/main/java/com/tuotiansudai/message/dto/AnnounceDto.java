package com.tuotiansudai.message.dto;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.message.repository.model.AnnounceModel;

import java.io.Serializable;
import java.util.Date;

public class AnnounceDto extends BaseDataDto implements Serializable {

    private long id;

    private String title;

    private String content;

    private String contentText;

    private boolean showOnHome;

    private Date createdTime = new Date();

    private Date updatedTime = new Date();

    public AnnounceDto() {
    }

    public AnnounceDto(AnnounceModel model) {
        this.id = model.getId();
        this.title = model.getTitle();
        this.content = model.getContent();
        this.showOnHome = model.isShowOnHome();
        this.createdTime = model.getCreatedTime();
        this.updatedTime = model.getUpdatedTime();
        this.contentText = model.getContentText();
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

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}
