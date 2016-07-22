package com.tuotiansudai.api.dto.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class LoanDetailRequestDto extends BaseParamDto {
    @NotEmpty(message = "0024")
    @Pattern(regexp = "^\\d+$", message = "0024")
    private String loanId;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
