package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanPeriodUnit;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.ProductType;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.math.BigDecimal;
import java.util.Date;

public class HomeLoanDto {

    private long id;

    private String name;

    private ProductType productType;

    private String activityType;

    private double baseRate;

    private double activityRate;

    private int periods;

    private boolean isPeriodMonthUnit;

    private String amount;

    private double progress;

    private String status;

    private Date fundraisingStartTime;

    private long preheatSeconds;

    public HomeLoanDto(long loanId, String name, ProductType productType, ActivityType activityType, LoanPeriodUnit periodUnit, double baseRate, double activityRate, int periods, long amount, long investAmount, LoanStatus status, Date fundraisingStartTime) {
        this.id = loanId;
        this.name = name;
        this.productType = productType;
        this.activityType = activityType.name();
        this.baseRate = new BigDecimal(String.valueOf(baseRate)).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
        if (activityRate > 0) {
            this.activityRate = new BigDecimal(String.valueOf(activityRate)).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
        }
        this.periods = periods;
        this.amount = new BigDecimal(amount).toString();
        this.isPeriodMonthUnit = periodUnit == LoanPeriodUnit.MONTH;
        this.progress = new BigDecimal(investAmount).divide(new BigDecimal(amount), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue();
        this.status = status.name();
        this.fundraisingStartTime = fundraisingStartTime;
        this.preheatSeconds = (fundraisingStartTime.getTime() - System.currentTimeMillis()) / 1000;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
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

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public boolean getIsPeriodMonthUnit() {
        return isPeriodMonthUnit;
    }

    public void setPeriodMonthUnit(boolean periodMonthUnit) {
        isPeriodMonthUnit = periodMonthUnit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
