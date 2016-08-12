package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.TransferStatus;

import java.util.List;

public class UserInvestListRequestDto extends BaseParamDto {
    private List<TransferStatus> transferStatus;

    public List<TransferStatus> getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(List<TransferStatus> transferStatus) {
        this.transferStatus = transferStatus;
    }
}
