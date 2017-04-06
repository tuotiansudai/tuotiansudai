package com.tuotiansudai.dto;

import com.tuotiansudai.enums.riskestimation.Estimate;

public class RiskEstimateDto extends BaseDataDto {

    private Estimate estimate;

    public RiskEstimateDto() {
    }

    public RiskEstimateDto(Estimate estimate) {
        this.estimate = estimate;
        this.status = estimate != null;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }
}
