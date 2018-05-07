package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class CancelCardBindRequestDto extends UserBaseRequestDto {

    public CancelCardBindRequestDto(String userName, String accountNo, String loginName, String mobile) {
        super(userName, accountNo, ApiType.CANCEL_CARD_BIND, loginName, mobile);
    }
}