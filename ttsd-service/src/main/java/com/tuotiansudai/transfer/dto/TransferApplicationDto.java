package com.tuotiansudai.transfer.dto;

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
