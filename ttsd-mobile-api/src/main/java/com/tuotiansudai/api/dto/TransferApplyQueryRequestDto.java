package com.tuotiansudai.api.dto;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class TransferApplyQueryRequestDto extends BaseParamDto {
    @NotEmpty(message = "0023")
    @Pattern(regexp = "^\\d+$", message = "0023")
    private String investId;

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }
}
