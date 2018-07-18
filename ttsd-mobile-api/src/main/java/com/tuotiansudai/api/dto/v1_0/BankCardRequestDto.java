package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class BankCardRequestDto extends BaseParamDto {

    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额", example = "11.00")
    @NotEmpty(message = "充值金额不能为空")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "充值金额不正确")
    private String rechargeAmount;

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
}
