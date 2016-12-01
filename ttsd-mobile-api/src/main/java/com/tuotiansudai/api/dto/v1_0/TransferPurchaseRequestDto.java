package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class TransferPurchaseRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "债权转让ID", example = "1001")
    private String transferApplicationId;

    public TransferPurchaseRequestDto(){}

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }
}