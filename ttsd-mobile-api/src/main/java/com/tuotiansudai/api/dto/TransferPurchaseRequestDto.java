package com.tuotiansudai.api.dto;

import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class TransferPurchaseRequestDto extends BaseParamDto {

    private String transferApplicationId;


    public TransferPurchaseRequestDto(){}

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }




}

