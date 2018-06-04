package com.tuotiansudai.fudian.dto.request;

import java.util.Map;

public class PasswordResetRequestDto extends NotifyRequestDto {

    public PasswordResetRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, Map<String, String> extraValues) {
        super(source, loginName, mobile, userName, accountNo);
    }
}