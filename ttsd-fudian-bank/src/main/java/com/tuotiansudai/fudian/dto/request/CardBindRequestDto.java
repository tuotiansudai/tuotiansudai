package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class CardBindRequestDto extends UserBaseRequestDto {

    public CardBindRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, Map<String, String> extraValues) {
        super(source, loginName, mobile, userName, accountNo, ApiType.CARD_BIND, extraValues);
    }
}