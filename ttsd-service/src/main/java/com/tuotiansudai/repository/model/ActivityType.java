package com.tuotiansudai.repository.model;

public enum ActivityType {
    NORMAL_INVEST("普通投资"),
    NOVICE_INVEST("新手投资"),
    DIRECTIONAL_INVEST("定向投资"),
    INCREASE_INTEREST_INVEST("加息投资");

    private String activityTypeName;
    private ActivityType(String activityTypeName){
        this.activityTypeName = activityTypeName;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }
}
