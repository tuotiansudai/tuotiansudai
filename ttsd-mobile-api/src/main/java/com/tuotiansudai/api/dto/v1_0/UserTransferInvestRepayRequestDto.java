package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;

public class UserTransferInvestRepayRequestDto extends BaseParamDto {

    @Pattern(regexp = "^\\d+$")
    @ApiModelProperty(value = "债权转让ID", example = "1001")
    private String transferApplicationId;

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }
}
