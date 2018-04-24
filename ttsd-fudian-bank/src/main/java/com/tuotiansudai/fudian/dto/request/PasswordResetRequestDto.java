package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class PasswordResetRequestDto extends UserBaseRequestDto {

    public PasswordResetRequestDto(String userName, String accountNo) {
        super(userName, accountNo, ApiType.PASSWORD_RESET.name());
    }
}