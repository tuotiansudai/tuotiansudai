package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class CardBindRequestDto extends UserBaseRequestDto {

    public CardBindRequestDto(String userName, String accountNo, String loginName, String mobile) {
        super(userName, accountNo, ApiType.CARD_BIND, loginName, mobile);
    }
}