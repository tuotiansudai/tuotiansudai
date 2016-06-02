package com.tuotiansudai.repository.model;

public enum ActivityType {
    NORMAL("普通投资"),
    NEWBIE("新手专享"),
    EXCLUSIVE("定向投资"),
    PROMOTION("加息投资");

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
