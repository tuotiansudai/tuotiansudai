package com.tuotiansudai.transfer.dto;

import com.tuotiansudai.repository.model.TransferStatus;

import java.util.Date;

public class TransferApplicationDto {

    private Long transferInvestId;

    private Long transferAmount;

    public TransferApplicationDto() {
    }

    public Long getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(Long transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public Long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Long transferAmount) {
        this.transferAmount = transferAmount;
    }

}
