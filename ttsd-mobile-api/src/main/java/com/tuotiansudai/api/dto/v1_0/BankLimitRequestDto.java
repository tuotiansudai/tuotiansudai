package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class BankLimitRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "银行卡缩写", example = "ICBC")
    private String bankCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
