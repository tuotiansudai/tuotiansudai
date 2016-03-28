package com.tuotiansudai.transfer.dto;

import com.tuotiansudai.repository.model.TransferStatus;

import java.util.Date;

public class TransferApplicationDto {


    private Long transferInvestId;

    private Long transferAmount;

    private Boolean transferInterest;

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

    public Boolean getTransferInterest() {
        return transferInterest;
    }

    public void setTransferInterest(Boolean transferInterest) {
        this.transferInterest = transferInterest;
    }

}
