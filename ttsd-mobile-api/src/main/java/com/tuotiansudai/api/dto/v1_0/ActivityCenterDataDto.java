package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.ActivityDto;

import java.util.List;

public class ActivityCenterDataDto extends BaseResponseDataDto {
    private List<ActivityDto> activities;

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDto> activities) {
        this.activities = activities;
    }
}
