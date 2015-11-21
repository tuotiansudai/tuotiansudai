package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanType;

public class LoanListWebDto {

    private long id;

    private String name;

    private Integer baseRateInteger;

    private Integer baseRateFraction;

    private Integer activityRateInteger;

    private Integer activityRateFraction;

    private long periods;

    private LoanType type;

    private LoanStatus status;

    private String loanAmount;

    private String added;

    private String rateOfAdvance;

    private ActivityType activityType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPeriods() {
        return periods;
    }

    public void setPeriods(long periods) {
        this.periods = periods;
    }

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getRateOfAdvance() {
        return rateOfAdvance;
    }

    public void setRateOfAdvance(String rateOfAdvance) {
        this.rateOfAdvance = rateOfAdvance;
    }

    public Integer getBaseRateInteger() {
        return baseRateInteger;
    }

    public void setBaseRateInteger(Integer baseRateInteger) {
        this.baseRateInteger = baseRateInteger;
    }

    public Integer getBaseRateFraction() {
        return baseRateFraction;
    }

    public void setBaseRateFraction(Integer baseRateFraction) {
        this.baseRateFraction = baseRateFraction;
    }

    public Integer getActivityRateInteger() {
        return activityRateInteger;
    }

    public void setActivityRateInteger(Integer activityRateInteger) {
        this.activityRateInteger = activityRateInteger;
    }

    public Integer getActivityRateFraction() {
        return activityRateFraction;
    }

    public void setActivityRateFraction(Integer activityRateFraction) {
        this.activityRateFraction = activityRateFraction;
    }
}
