package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class InvestDetailRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "出借ID", example = "11111")
    private String investId;

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }
}
