package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class AuthorizationRequestDto extends UserBaseRequestDto {

    private String businessType = "1"; //1授权 2取消授权

    public AuthorizationRequestDto(Source source, String loginName, String mobile, String userName, String accountNo) {
        super(source, loginName, mobile, userName, accountNo, ApiType.AUTHORIZATION);
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}