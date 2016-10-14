package com.tuotiansudai.api.dto.v1_0;

import javax.validation.constraints.Pattern;

public class UserTransferInvestRepayRequestDto extends BaseParamDto {

    @Pattern(regexp = "^\\d+$")
    private String transferApplicationId;

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }
}
