package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class AutoInvestPlanInfoResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "是否授权自动投标", example = "true")
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
