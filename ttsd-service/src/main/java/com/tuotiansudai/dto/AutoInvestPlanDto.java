package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class AutoInvestPlanDto implements Serializable {
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private long minInvestAmount;

    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private long maxInvestAmount;

    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private long retentionAmount;

    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private int autoInvestPeriods;

    public long getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(long minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public long getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(long maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public long getRetentionAmount() {
        return retentionAmount;
    }

    public void setRetentionAmount(long retentionAmount) {
        this.retentionAmount = retentionAmount;
    }

    public int getAutoInvestPeriods() {
        return autoInvestPeriods;
    }

    public void setAutoInvestPeriods(int autoInvestPeriods) {
        this.autoInvestPeriods = autoInvestPeriods;
    }
}
