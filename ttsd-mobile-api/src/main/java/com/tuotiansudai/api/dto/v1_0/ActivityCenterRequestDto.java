package com.tuotiansudai.api.dto.v1_0;

public class ActivityCenterRequestDto extends BaseParamDto {
    private Integer index;
    private Integer pageSize;
    private ActivityCenterType activityType;

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

    public ActivityCenterType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityCenterType activityCenterType) {
        this.activityType = activityCenterType;
    }
}
