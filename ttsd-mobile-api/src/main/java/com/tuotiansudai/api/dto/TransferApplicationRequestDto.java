package com.tuotiansudai.api.dto;

import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class TransferApplicationRequestDto extends BaseParamDto{

    @NotEmpty(message = "0023")
    @Pattern(regexp = "^\\d+$",message = "0023")
    private String transferInvestId;
    @NotEmpty(message = "0023")
    private String transferAmount;

    private Boolean transferInterest;

    public TransferApplicationRequestDto() {
    }

    public String getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(String transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Boolean isTransferInterest() {
        return transferInterest;
    }

    public void setTransferInterest(Boolean transferInterest) {
        this.transferInterest = transferInterest;
    }

    public TransferApplicationDto convertToTransferApplicationDto(){
        TransferApplicationDto transferApplicationDto = new TransferApplicationDto();
        transferApplicationDto.setTransferInvestId(Long.parseLong(this.transferInvestId));
        transferApplicationDto.setTransferInterest(this.transferInterest == null?false:this.transferInterest);
        transferApplicationDto.setTransferAmount(AmountConverter.convertStringToCent(this.transferAmount));
        return transferApplicationDto;
    }
}
