package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class TransferApplicationDetailRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "债权转让ID", example = "1")
    private String transferApplicationId;

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

}

