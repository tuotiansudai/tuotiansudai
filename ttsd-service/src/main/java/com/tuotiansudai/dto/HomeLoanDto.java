package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;

import java.math.BigDecimal;

public class HomeLoanDto {

    private long id;

    private String name;

    private String activityType;

    private Integer baseRateInteger;

    private Integer baseRateFraction;

    private Integer activityRateInteger;

    private Integer activityRateFraction;

    private int periods;

    private String amount;

    private String progress;

    private String status;

    public HomeLoanDto(long loanId, String name, ActivityType activityType, double baseRate, double activityRate, int periods, long amount, long investAmount, LoanStatus status) {
        this.id = loanId;
        this.name = name;
        this.activityType = activityType.name();
        String baseRatePercentage = new BigDecimal(String.valueOf(baseRate)).multiply(new BigDecimal("100")).setScale(2).toString();
        String activityPercentage = new BigDecimal(String.valueOf(activityRate)).multiply(new BigDecimal("100")).setScale(2).toString();
        this.baseRateInteger = Integer.parseInt(baseRatePercentage.split("\\.")[0]);
        this.baseRateFraction = Integer.parseInt(baseRatePercentage.split("\\.")[1]) == 0 ? null : Integer.parseInt(baseRatePercentage.split("\\.")[1]);
        if (activityRate > 0) {
            this.activityRateInteger = Integer.parseInt(activityPercentage.split("\\.")[0]);
            this.activityRateFraction = Integer.parseInt(activityPercentage.split("\\.")[1]) == 0 ? null : Integer.parseInt(activityPercentage.split("\\.")[1]);
        }
        this.periods = periods;
        this.amount = String.valueOf(new BigDecimal(amount).divide(new BigDecimal(1000000), 2, BigDecimal.ROUND_DOWN).intValue());
        this.progress = String.valueOf(new BigDecimal(investAmount).divide(new BigDecimal(amount), 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).intValue());
        this.status = status.name();
    }

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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
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

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
