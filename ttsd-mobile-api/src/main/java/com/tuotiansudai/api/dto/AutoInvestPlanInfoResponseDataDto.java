package com.tuotiansudai.api.dto;

public class AutoInvestPlanInfoResponseDataDto extends BaseResponseDataDto{
    private boolean autoInvest;

    private AutoInvestPlanDataDto autoInvestPlan;

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public AutoInvestPlanDataDto getAutoInvestPlan() {
        return autoInvestPlan;
    }

    public void setAutoInvestPlan(AutoInvestPlanDataDto autoInvestPlan) {
        this.autoInvestPlan = autoInvestPlan;
    }
}
