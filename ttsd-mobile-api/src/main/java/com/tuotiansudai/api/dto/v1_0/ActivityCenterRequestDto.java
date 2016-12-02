package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class ActivityCenterRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "起始条数", example = "1")
    private Integer index;

    @ApiModelProperty(value = "查询条数", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "活动中心类型", example = "CURRENT,PREVIOUS")
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
