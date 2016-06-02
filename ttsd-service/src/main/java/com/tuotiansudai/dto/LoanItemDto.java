package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.ProductType;

import java.util.Date;

public class LoanItemDto {

    private long id;

    private String name;

    private ProductType productType;

    private double baseRate;

    private double activityRate;

    private double interestCouponRate;

    private long periods;

    private LoanType type;

    private LoanStatus status;

    private long loanAmount;

    private String alert;

    private double progress;

    private ActivityType activityType;

    private Date fundraisingStartTime;

    private long preheatSeconds;

    private int duration;

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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public double getInterestCouponRate() {
        return interestCouponRate;
    }

    public void setInterestCouponRate(double interestCouponRate) {
        this.interestCouponRate = interestCouponRate;
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

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public void setFundraisingStartTime(Date fundraisingStartTime) {
        this.fundraisingStartTime = fundraisingStartTime;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public void setPreheatSeconds(long preheatSeconds) {
        this.preheatSeconds = preheatSeconds;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
