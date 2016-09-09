package com.tuotiansudai.api.dto.v1_0;

import javax.validation.constraints.Pattern;

public class UserInvestRepayRequestDto extends BaseParamDto {

    @Pattern(regexp = "^\\d+$")
    private String investId;

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }
}
