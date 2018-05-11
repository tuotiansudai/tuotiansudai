package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class PasswordResetRequestDto extends UserBaseRequestDto {

    public PasswordResetRequestDto(Source source, String loginName, String mobile, String userName, String accountNo) {
        super(source, loginName, mobile, userName, accountNo, ApiType.PASSWORD_RESET);
    }
}