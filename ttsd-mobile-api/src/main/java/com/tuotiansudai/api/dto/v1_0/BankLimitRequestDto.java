package com.tuotiansudai.api.dto.v1_0;

public class BankLimitRequestDto extends BaseParamDto {
    private String bankCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
