package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class UserTransferInvestRepayResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "回款计划", example = "list")
    private List<TransferInvestRepayDataDto> transferInvestRepays = new ArrayList<>();

    public List<TransferInvestRepayDataDto> getTransferInvestRepays() {
        return transferInvestRepays;
    }

    public void setTransferInvestRepays(List<TransferInvestRepayDataDto> transferInvestRepays) {
        this.transferInvestRepays = transferInvestRepays;
    }
}
