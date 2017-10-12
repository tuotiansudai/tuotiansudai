package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class TransferApplicationDetailRequestDto extends BaseParamDto {

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    @ApiModelProperty(value = "债权转让ID", example = "1")
    private long transferApplicationId;


    public long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }
}

