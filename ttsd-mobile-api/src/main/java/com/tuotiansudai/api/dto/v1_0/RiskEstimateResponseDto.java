package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.enums.riskestimation.Estimate;
import io.swagger.annotations.ApiModelProperty;

public class RiskEstimateResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "测评结果", example = "list")
    private String riskEstimate;

    @ApiModelProperty(value = "测评结果描述", example = "list")
    private String riskEstimateDesc;

    public RiskEstimateResponseDto() {
    }

    public RiskEstimateResponseDto(Estimate estimate) {
        this.riskEstimate = estimate.getType();
        this.riskEstimateDesc = estimate.getDescription();
    }

    public String getRiskEstimate() {
        return riskEstimate;
    }

    public void setRiskEstimate(String riskEstimate) {
        this.riskEstimate = riskEstimate;
    }

    public String getRiskEstimateDesc() {
        return riskEstimateDesc;
    }

    public void setRiskEstimateDesc(String riskEstimateDesc) {
        this.riskEstimateDesc = riskEstimateDesc;
    }
}
