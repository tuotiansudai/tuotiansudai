package com.tuotiansudai.api.dto;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;

public class TransferPurchaseRequestDto extends BaseParamDto {
    private String transferApplicationId;

    public TransferPurchaseRequestDto(){}

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }
}