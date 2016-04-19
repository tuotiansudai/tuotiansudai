package com.tuotiansudai.api.dto;

public class TransferPurchaseRequestDto extends BaseParamDto {

    private Long transferApplicationId;

    public Long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(Long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

}
