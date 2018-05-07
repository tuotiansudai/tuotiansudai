package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class PasswordResetRequestDto extends UserBaseRequestDto {

    public PasswordResetRequestDto(String userName, String accountNo, String loginName, String mobile) {
        super(userName, accountNo, ApiType.PASSWORD_RESET, loginName, mobile);
    }
}