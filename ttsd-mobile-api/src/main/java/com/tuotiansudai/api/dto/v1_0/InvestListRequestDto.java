package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class InvestListRequestDto extends BaseParamDto {
    @NotEmpty(message = "0023")
    @Pattern(regexp = "^\\d+$",message = "0023")
    @ApiModelProperty(value = "标的ID", example = "11111")
    private String loanId;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

}
