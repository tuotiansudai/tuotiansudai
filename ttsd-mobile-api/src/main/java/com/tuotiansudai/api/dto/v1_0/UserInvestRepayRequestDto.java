package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;

public class UserInvestRepayRequestDto extends BaseParamDto {

    @Pattern(regexp = "^\\d+$")
    @ApiModelProperty(value = "出借ID", example = "")
    private String investId;

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }
}
