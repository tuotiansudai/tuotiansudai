package com.tuotiansudai.repository.model;

public enum ActivityType {
    NORMAL("普通出借"),
    NEWBIE("新手专享"),
    EXCLUSIVE("定向出借"),
    PROMOTION("加息出借");

    private String activityTypeName;

    ActivityType(String activityTypeName){
        this.activityTypeName = activityTypeName;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }
}
