package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

public class AutoInvestPlanDto implements Serializable {
    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String minInvestAmount;

    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String maxInvestAmount;

    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String retentionAmount;

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private int autoInvestPeriods;

    private boolean enabled;

    private boolean isTodayPlan;

    public AutoInvestPlanDto() {
    }

    public AutoInvestPlanDto(AutoInvestPlanModel model) {
        this.minInvestAmount = AmountConverter.convertCentToString(model.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(model.getMaxInvestAmount());
        this.retentionAmount = AmountConverter.convertCentToString(model.getRetentionAmount());
        this.autoInvestPeriods = model.getAutoInvestPeriods();
        this.enabled = model.isEnabled();

        Date startOfToday = new DateTime().withTimeAtStartOfDay().toDate();
        // 是否为当天创建的计划
        this.isTodayPlan = model.getCreatedTime().after(startOfToday);
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(String minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(String maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public String getRetentionAmount() {
        return retentionAmount;
    }

    public void setRetentionAmount(String retentionAmount) {
        this.retentionAmount = retentionAmount;
    }

    public int getAutoInvestPeriods() {
        return autoInvestPeriods;
    }

    public void setAutoInvestPeriods(int autoInvestPeriods) {
        this.autoInvestPeriods = autoInvestPeriods;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTodayPlan() {
        return isTodayPlan;
    }

    public void setIsTodayPlan(boolean isTodayPlan) {
        this.isTodayPlan = isTodayPlan;
    }
}
