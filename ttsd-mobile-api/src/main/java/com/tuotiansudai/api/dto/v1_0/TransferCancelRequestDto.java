package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class TransferCancelRequestDto extends BaseParamDto {
    @NotEmpty
    @ApiModelProperty(value = "债权转让ID", example = "1001")
    private long transferApplicationId;

    public long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

}

