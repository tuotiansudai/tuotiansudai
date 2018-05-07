package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class CancelCardBindRequestDto extends UserBaseRequestDto {

    public CancelCardBindRequestDto(String loginName, String mobile, String userName, String accountNo) {
        super(loginName, mobile, userName, accountNo, ApiType.CANCEL_CARD_BIND);
    }
}