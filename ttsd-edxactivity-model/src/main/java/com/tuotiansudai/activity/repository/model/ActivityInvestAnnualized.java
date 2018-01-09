package com.tuotiansudai.activity.repository.model;


public enum ActivityInvestAnnualized {

    NEW_YEAR_ACTIVITY("新年更有钱活动", "逢万返百"),
    ;


    ActivityInvestAnnualized(String activityName, String activityDesc) {
        this.activityName = activityName;
        this.activityDesc = activityDesc;
    }

    private String activityName;
    private String activityDesc;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }
}
