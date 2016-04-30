package com.tuotiansudai.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class InvestListRequestDto extends BaseParamDto {
    @NotEmpty(message = "0023")
    @Pattern(regexp = "^\\d+$",message = "0023")
    private String loanId;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

}
