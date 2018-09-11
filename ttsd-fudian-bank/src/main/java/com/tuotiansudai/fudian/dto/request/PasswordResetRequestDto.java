package com.tuotiansudai.fudian.dto.request;

public class PasswordResetRequestDto extends NotifyRequestDto {

    public PasswordResetRequestDto(Source source, String loginName, String mobile, String userName, String accountNo) {
        super(source, loginName, mobile, userName, accountNo);
    }
}