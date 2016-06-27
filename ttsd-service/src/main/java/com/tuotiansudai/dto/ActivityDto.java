package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public class ActivityDto {
    private long id;
    private String title;
    private String webActivityUrl;
    private String appActivityUrl;
    private String description;
    private String webPictureUrl;
    private String appPictureUrl;
    private Date activatedTime;
    private Date expiredTime;
    private List<Source> source;
    private ActivityStatus status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String activatedBy;

    public ActivityDto() {
    }

    public ActivityDto(ActivityModel activityModel) {
        this.id = activityModel.getId();
        this.title = activityModel.getTitle();
        this.webActivityUrl = activityModel.getWebActivityUrl();
        this.appActivityUrl = activityModel.getAppActivityUrl();
        this.description = activityModel.getDescription();
        this.webPictureUrl = activityModel.getWebPictureUrl();
        this.appPictureUrl = activityModel.getAppPictureUrl();
        this.activatedTime = activityModel.getActivatedTime();
        this.expiredTime = activityModel.getExpiredTime();
        this.source = activityModel.getSource();
        this.status = activityModel.getStatus();
        this.createdBy = activityModel.getCreatedBy();
        this.createdTime = activityModel.getCreatedTime();
        this.updatedBy = activityModel.getUpdatedBy();
        this.updatedTime = activityModel.getUpdatedTime();
        this.activatedBy = activityModel.getActivatedBy();
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

    public String getWebActivityUrl() {
        return webActivityUrl;
    }

    public void setWebActivityUrl(String webActivityUrl) {
        this.webActivityUrl = webActivityUrl;
    }

    public String getAppActivityUrl() {
        return appActivityUrl;
    }

    public void setAppActivityUrl(String appActivityUrl) {
        this.appActivityUrl = appActivityUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebPictureUrl() {
        return webPictureUrl;
    }

    public void setWebPictureUrl(String webPictureUrl) {
        this.webPictureUrl = webPictureUrl;
    }

    public String getAppPictureUrl() {
        return appPictureUrl;
    }

    public void setAppPictureUrl(String appPictureUrl) {
        this.appPictureUrl = appPictureUrl;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public List<Source> getSource() {
        return source;
    }

    public void setSource(List<Source> source) {
        this.source = source;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(String activatedBy) {
        this.activatedBy = activatedBy;
    }
}
