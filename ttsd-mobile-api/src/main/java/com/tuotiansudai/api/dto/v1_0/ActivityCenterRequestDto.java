package com.tuotiansudai.api.dto.v1_0;

public class ActivityCenterRequestDto extends BaseParamDto {
    private Integer index = 1;
    private Integer pageSize = 10;
    private ActivityType activityType;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
}
