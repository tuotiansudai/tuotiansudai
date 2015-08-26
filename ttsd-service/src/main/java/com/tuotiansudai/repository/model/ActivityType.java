package com.tuotiansudai.repository.model;

public enum ActivityType {
    /***默认值***/
    NORMAL_INVEST("NORMAL","普通投资"),
    NOVICE_INVEST("NOVICE","新手投资"),
    DIRECTIONAL_INVEST("EXCLUSIVE","定向投资"),
    INCREASE_INTEREST_INVEST("PROMOTION","加息投资");

    private String activityTypeCode;
    private String activityTypeName;
    private ActivityType(String activityTypeCode,String activityTypeName){
        this.activityTypeCode = activityTypeCode;
        this.activityTypeName = activityTypeName;
    }

    public String getActivityTypeCode() {
        return activityTypeCode;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }
}
