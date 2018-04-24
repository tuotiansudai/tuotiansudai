package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class AuthorizationRequestDto extends UserBaseRequestDto {

    private String businessType = "1"; //1授权 2取消授权

    public AuthorizationRequestDto(String userName, String accountNo) {
        super(userName, accountNo, ApiType.AUTHORIZATION.name());
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}