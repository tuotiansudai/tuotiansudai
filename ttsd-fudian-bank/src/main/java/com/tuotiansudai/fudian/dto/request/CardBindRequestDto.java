package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class CardBindRequestDto extends UserBaseRequestDto {

    public CardBindRequestDto(String loginName, String mobile, String userName, String accountNo) {
        super(loginName, mobile, userName, accountNo, ApiType.CARD_BIND);
    }
}