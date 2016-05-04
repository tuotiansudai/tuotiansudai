package com.tuotiansudai.dto;

import com.tuotiansudai.coupon.repository.model.CouponModel;
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

    private ActivityType activityType;

    private double baseRate;

    private double activityRate;

    private int periods;

    private boolean isPeriodMonthUnit;

    private String amount;

    private double progress;

    private String status;

    private Date fundraisingStartTime;

    private long preheatSeconds;

    private double newbieInterestCouponRate;

    public HomeLoanDto(CouponModel newbieInterestCouponModel, long loanId, String name, ProductType productType, ActivityType activityType, LoanPeriodUnit periodUnit, double baseRate, double activityRate, int periods, long amount, long investAmount, LoanStatus status, Date fundraisingStartTime) {
        this.id = loanId;
        this.name = name;
        this.productType = productType;
        this.activityType = activityType;
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
        if (newbieInterestCouponModel != null && newbieInterestCouponModel.getProductTypes().contains(productType)) {
            this.newbieInterestCouponRate = newbieInterestCouponModel.getRate();
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public int getPeriods() {
        return periods;
    }

    public boolean getIsPeriodMonthUnit() {
        return isPeriodMonthUnit;
    }

    public String getAmount() {
        return amount;
    }

    public double getProgress() {
        return progress;
    }

    public String getStatus() {
        return status;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public double getNewbieInterestCouponRate() {
        return newbieInterestCouponRate;
    }
}
