package com.tuotiansudai.fudian.dto.request;

import java.util.Map;

public class AuthorizationRequestDto extends NotifyRequestDto {

    private String businessType = "1"; //1授权 2取消授权

    public AuthorizationRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, Map<String, String> extraValues) {
        super(source, loginName, mobile, userName, accountNo);
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}