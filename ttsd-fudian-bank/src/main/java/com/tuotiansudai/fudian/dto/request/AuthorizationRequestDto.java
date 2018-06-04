package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankBaseDto;

public class AuthorizationRequestDto extends NotifyRequestDto {

    private String businessType = "1"; //1授权 2取消授权

    public AuthorizationRequestDto(Source source, BankBaseDto bankBaseDto) {
        super(source, bankBaseDto.getLoginName(), bankBaseDto.getMobile(), bankBaseDto.getBankUserName(), bankBaseDto.getBankAccountNo());
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}