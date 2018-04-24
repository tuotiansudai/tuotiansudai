package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class CancelCardBindRequestDto extends UserBaseRequestDto {

    public CancelCardBindRequestDto(String userName, String accountNo) {
        super(userName, accountNo, ApiType.CANCEL_CARD_BIND.name());
    }
}