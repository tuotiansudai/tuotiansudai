package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ActivityModel;

public class ActivityCenterDataDto {
    private String descTitle;
    private String imageUrl;
    private String activityUrl;

    public ActivityCenterDataDto() {
    }

    public ActivityCenterDataDto(ActivityModel activityModel) {
        this.descTitle = activityModel.getDescription();
        this.imageUrl = activityModel.getAppPictureUrl();
        this.activityUrl = activityModel.getAppActivityUrl();
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
}
