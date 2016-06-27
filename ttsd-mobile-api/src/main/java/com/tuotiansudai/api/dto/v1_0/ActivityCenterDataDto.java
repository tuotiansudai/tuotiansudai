package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.ActivityDto;

public class ActivityCenterDataDto {
    private String descTitle;
    private String imageUrl;
    private String activityUrl;

    public ActivityCenterDataDto() {
    }

    public ActivityCenterDataDto(ActivityDto activityDto) {
        this.descTitle = activityDto.getDescription();
        this.imageUrl = activityDto.getAppPictureUrl();
        this.activityUrl = activityDto.getAppActivityUrl();
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
