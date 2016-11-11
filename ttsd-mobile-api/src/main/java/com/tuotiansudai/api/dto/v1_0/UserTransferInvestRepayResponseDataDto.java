package com.tuotiansudai.api.dto.v1_0;

import java.util.ArrayList;
import java.util.List;

public class UserTransferInvestRepayResponseDataDto extends BaseResponseDataDto {

    private List<TransferInvestRepayDataDto> transferInvestRepays = new ArrayList<>();

    public List<TransferInvestRepayDataDto> getTransferInvestRepays() {
        return transferInvestRepays;
    }

    public void setTransferInvestRepays(List<TransferInvestRepayDataDto> transferInvestRepays) {
        this.transferInvestRepays = transferInvestRepays;
    }
}
