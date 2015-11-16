package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;

import java.math.BigDecimal;

public class HomeLoanDto {

    private long id;

    private String name;

    private String activityType;

    private String baseRate;

    private String activityRate;

    private int periods;

    private String amount;

    private String progress;

    private String status;

    public HomeLoanDto(long loanId, String name, ActivityType activityType, double baseRate, double activityRate, int periods, long amount, long investAmount, LoanStatus status) {
        this.id = loanId;
        this.name = name;
        this.activityType = activityType.name();
        double baseRatePercentage = new BigDecimal(baseRate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
        double activityPercentage = new BigDecimal(activityRate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
        if (baseRatePercentage == (long) baseRatePercentage)
            this.baseRate = String.format("%d", (long) baseRatePercentage);
        else
            this.baseRate = String.format("%s", baseRatePercentage);
        if (activityRate > 0) {
            if (activityPercentage == (long) activityPercentage)
                this.activityRate = String.format("%d", (long) activityPercentage);
            else
                this.activityRate = String.format("%s", activityPercentage);
        }
        this.periods = periods;
        this.amount = String.valueOf(new BigDecimal(amount).divide(new BigDecimal(100 * 10000), 2, BigDecimal.ROUND_DOWN).intValue());
        this.progress = String.valueOf(new BigDecimal(investAmount).divide(new BigDecimal(amount), 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).longValue());
        this.status = status.name();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public int getPeriods() {
        return periods;
    }

    public String getAmount() {
        return amount;
    }

    public String getProgress() {
        return progress;
    }

    public String getStatus() {
        return status;
    }
}
