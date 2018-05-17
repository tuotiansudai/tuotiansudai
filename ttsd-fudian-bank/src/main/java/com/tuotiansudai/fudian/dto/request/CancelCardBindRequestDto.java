package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class CancelCardBindRequestDto extends UserBaseRequestDto {

    public CancelCardBindRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, Map<String, String> extraValues) {
        super(source, loginName, mobile, userName, accountNo, ApiType.CANCEL_CARD_BIND, extraValues);
    }
}