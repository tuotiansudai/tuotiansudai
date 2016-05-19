package com.tuotiansudai.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class TransferCancelRequestDto extends BaseParamDto {
    @NotEmpty
    private long transferApplicationId;

    public long getTransferApplicationId() { return transferApplicationId; }

    public void setTransferApplicationId(long transferApplicationId) { this.transferApplicationId = transferApplicationId; }

}

