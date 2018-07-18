package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class WithdrawOperateRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "提现金额", example = "100")
    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

}
