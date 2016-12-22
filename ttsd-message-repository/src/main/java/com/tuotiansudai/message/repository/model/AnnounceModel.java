package com.tuotiansudai.message.repository.model;

import java.io.Serializable;
import java.util.Date;

public class AnnounceModel implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String contentText;

    private boolean showOnHome;

    private Date createdTime = new Date();

    private Date updatedTime = new Date();

    public Long getId() {
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

    public AnnounceModel(){

    }

    public AnnounceModel(Long id, String title, String content, String contentText, boolean showOnHome) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.contentText = contentText;
        this.showOnHome = showOnHome;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}
