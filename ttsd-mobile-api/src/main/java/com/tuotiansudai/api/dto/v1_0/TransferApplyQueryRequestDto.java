package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class TransferApplyQueryRequestDto extends BaseParamDto {
    @NotEmpty(message = "0023")
    @Pattern(regexp = "^\\d+$", message = "0023")
    @ApiModelProperty(value = "出借ID", example = "")
    private String investId;

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }
}
