package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ActivityModel;

public class ActivityCenterDataDto {
    private String title;
    private String imageUrl;
    private String activityUrl;

    public ActivityCenterDataDto() {
    }

    public ActivityCenterDataDto(ActivityModel activityModel) {
        this.title = activityModel.getTitle();
        this.imageUrl = activityModel.getAppPictureUrl();
        this.activityUrl = activityModel.getAppActivityUrl();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
