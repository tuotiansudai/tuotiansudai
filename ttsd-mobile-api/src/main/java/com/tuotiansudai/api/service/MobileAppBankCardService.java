package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BankCardReplaceRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.repository.model.BankCardModel;

public interface MobileAppBankCardService {
    /**
     * @param userId 绑卡或签约用户ID
     * @return boolean
     * @function 查询绑卡状态
     */
    boolean queryBindAndSginStatus(String userId, String operationType);

    BaseResponseDto generateBankCardResponse(BankCardReplaceRequestDto requestDto);

    void save(BankCardModel bankCard);
}
