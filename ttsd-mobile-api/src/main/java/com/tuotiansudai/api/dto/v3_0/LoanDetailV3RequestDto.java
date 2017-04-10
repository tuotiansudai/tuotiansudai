package com.tuotiansudai.api.dto.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class LoanDetailV3RequestDto extends BaseParamDto {
    @NotEmpty(message = "0024")
    @Pattern(regexp = "^\\d+$", message = "0024")
    @ApiModelProperty(value = "标的ID", example = "1001")
    private String loanId;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
