package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.utils.AmountUtil;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Calendar;
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

    private boolean freshPlan;

    public AutoInvestPlanDto() {
    }

    public AutoInvestPlanDto(AutoInvestPlanModel model) {
        this.minInvestAmount = AmountUtil.convertCentToString(model.getMinInvestAmount());
        this.maxInvestAmount = AmountUtil.convertCentToString(model.getMaxInvestAmount());
        this.retentionAmount = AmountUtil.convertCentToString(model.getRetentionAmount());
        this.autoInvestPeriods = model.getAutoInvestPeriods();
        this.enabled = model.isEnabled();

        // today 0:0:0
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        this.freshPlan = model.getCreatedTime().after(cal.getTime());
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

    public boolean isFreshPlan() {
        return freshPlan;
    }

    public void setFreshPlan(boolean freshPlan) {
        this.freshPlan = freshPlan;
    }
}
