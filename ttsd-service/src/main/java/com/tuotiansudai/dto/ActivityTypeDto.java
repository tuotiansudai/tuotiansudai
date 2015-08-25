package com.tuotiansudai.dto;

public class ActivityTypeDto {
    //活动类型代号
    private String activityTypeCode;
    //活动类型名称
    private String activityTypeName;

    public ActivityTypeDto(String activityTypeCode,String activityTypeName){
        this.activityTypeCode = activityTypeCode;
        this.activityTypeName = activityTypeName;
    }

    public String getActivityTypeCode() {
        return activityTypeCode;
    }

    public void setActivityTypeCode(String activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }
}
