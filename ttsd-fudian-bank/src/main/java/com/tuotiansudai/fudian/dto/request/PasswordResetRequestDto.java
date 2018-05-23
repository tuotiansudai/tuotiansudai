package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class PasswordResetRequestDto extends UserBaseRequestDto {

    public PasswordResetRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, Map<String, String> extraValues) {
        super(source, loginName, mobile, userName, accountNo, ApiType.PASSWORD_RESET, extraValues);
    }
}