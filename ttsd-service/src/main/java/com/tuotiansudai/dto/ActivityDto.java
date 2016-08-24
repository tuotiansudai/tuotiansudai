package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class ActivityDto {
    private Long activityId;
    private String title;
    private Long seq;
    private String webActivityUrl;
    private String appActivityUrl;
    private String description;
    private String webPictureUrl;
    private String appPictureUrl;
    private String longTerm;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date activatedTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
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

    public ActivityDto() {
    }

    public ActivityDto(ActivityModel activityModel) {
        this.activityId = activityModel.getId();
        this.title = activityModel.getTitle();
        this.seq = activityModel.getSeq();
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
        this.shareTitle = activityModel.getShareTitle();
        this.shareContent = activityModel.getShareContent();
        this.shareUrl = activityModel.getShareUrl();
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
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

    public String getLongTerm() {
        return longTerm;
    }

    public void setLongTerm(String longTerm) {
        this.longTerm = longTerm;
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
}
