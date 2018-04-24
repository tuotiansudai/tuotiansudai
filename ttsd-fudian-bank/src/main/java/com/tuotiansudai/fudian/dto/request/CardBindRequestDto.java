package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class CardBindRequestDto extends UserBaseRequestDto {

    public CardBindRequestDto(String userName, String accountNo) {
        super(userName, accountNo, ApiType.CARD_BIND.name());
    }
}