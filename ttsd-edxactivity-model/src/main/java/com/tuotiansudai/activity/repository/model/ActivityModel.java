package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.activity.repository.dto.ActivityDto;
import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ActivityModel implements Serializable {
    private Long id;
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
    private String shareTitle;
    private String shareContent;
    private String shareUrl;
    private Long seq;
    private boolean longTerm;

    public ActivityModel() {

    }

    public ActivityModel(ActivityDto activityDto) {
        this.title = activityDto.getTitle();
        this.webActivityUrl = activityDto.getWebActivityUrl();
        this.appActivityUrl = activityDto.getAppActivityUrl();
        this.description = activityDto.getDescription();
        this.webPictureUrl = activityDto.getWebPictureUrl();
        this.appPictureUrl = activityDto.getAppPictureUrl();
        this.activatedTime = activityDto.getActivatedTime();
        this.expiredTime = activityDto.getExpiredTime();
        this.source = activityDto.getSource();
        this.shareTitle = activityDto.getShareTitle();
        this.shareContent = activityDto.getShareContent();
        this.shareUrl = activityDto.getShareUrl();
        this.seq = activityDto.getSeq();
        this.longTerm = "longTerm".equals(activityDto.getLongTerm());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public boolean isLongTerm() {
        return longTerm;
    }

    public void setLongTerm(boolean longTerm) {
        this.longTerm = longTerm;
    }
}
