package com.tuotiansudai.repository.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuotian on 15/8/18.
 */
public enum ActivityType {
    /***默认值***/
    NORMAL_INVEST("normalInvest","普通投资"),
    NOVICE_INVEST("noviceInvest","新手投资"),
    DIRECTIONAL_INVEST("directionalInvest","定向投资"),
    INCREASE_INTEREST_INVEST("increaseInterestInvest","加息投资");

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
