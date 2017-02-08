package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

public class TransferApplicationDto {

    private Long transferInvestId;

    private Long transferAmount;

    private Source source = Source.WEB;

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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
