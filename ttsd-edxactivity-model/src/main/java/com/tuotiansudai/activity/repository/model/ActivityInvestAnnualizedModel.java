package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;

public class ActivityInvestAnnualizedModel implements Serializable {

    private long id;
    private String userName;
    private String loginName;
    private String mobile;
    private long sumInvestAmount;
    private long sumAnnualizedAmount;
    private ActivityInvestAnnualized activityInvestAnnualized;
    private String activityLoanDesc;

    public ActivityInvestAnnualizedModel() {
    }

    public ActivityInvestAnnualizedModel(String userName, String loginName, String mobile, long sumInvestAmount, long sumAnnualizedAmount, ActivityInvestAnnualized activityName, String activityLoanDesc) {
        this.userName = userName;
        this.loginName = loginName;
        this.mobile = mobile;
        this.sumInvestAmount = sumInvestAmount;
        this.sumAnnualizedAmount = sumAnnualizedAmount;
        this.activityInvestAnnualized = activityName;
        this.activityLoanDesc = activityLoanDesc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getSumInvestAmount() {
        return sumInvestAmount;
    }

    public void setSumInvestAmount(long sumInvestAmount) {
        this.sumInvestAmount = sumInvestAmount;
    }

    public long getSumAnnualizedAmount() {
        return sumAnnualizedAmount;
    }

    public void setSumAnnualizedAmount(long sumAnnualizedAmount) {
        this.sumAnnualizedAmount = sumAnnualizedAmount;
    }

    public ActivityInvestAnnualized getActivityInvestAnnualized() {
        return activityInvestAnnualized;
    }

    public void setActivityInvestAnnualized(ActivityInvestAnnualized activityInvestAnnualized) {
        this.activityInvestAnnualized = activityInvestAnnualized;
    }

    public String getActivityLoanDesc() {
        return activityLoanDesc;
    }

    public void setActivityLoanDesc(String activityLoanDesc) {
        this.activityLoanDesc = activityLoanDesc;
    }
}
