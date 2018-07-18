package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class BankRechargeRequestDto extends BaseParamDto {

    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额", example = "11.00")
    @NotEmpty(message = "充值金额不能为空")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "充值金额不正确")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
