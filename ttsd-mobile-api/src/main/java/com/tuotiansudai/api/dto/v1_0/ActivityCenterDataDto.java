package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ActivityModel;

public class ActivityCenterDataDto {
    private String descTitle;
    private String imageUrl;
    private String activityUrl;
    private String title;
    private String sharedUrl;
    private String content;


    public ActivityCenterDataDto() {
    }

    public ActivityCenterDataDto(ActivityModel activityModel) {
        this.descTitle = activityModel.getDescription();
        this.imageUrl = activityModel.getAppPictureUrl();
        this.activityUrl = activityModel.getAppActivityUrl();
        this.title = activityModel.getTitle();
        this.content = activityModel.getShareContent();
        this.sharedUrl = activityModel.getShareUrl();
    }

    public String getDescTitle() {
        return descTitle;
    }

    public void setDescTitle(String descTitle) {
        this.descTitle = descTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSharedUrl() {
        return sharedUrl;
    }

    public void setSharedUrl(String sharedUrl) {
        this.sharedUrl = sharedUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
