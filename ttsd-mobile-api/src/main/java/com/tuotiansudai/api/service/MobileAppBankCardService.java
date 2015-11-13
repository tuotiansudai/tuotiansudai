package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BankCardReplaceRequestDto;
import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.repository.model.BankCardModel;

public interface MobileAppBankCardService {

    BaseResponseDto bindBankCard(BankCardRequestDto requestDto);

    boolean queryBindAndSginStatus(String userId, String operationType);

    BaseResponseDto generateBankCardResponse(BankCardReplaceRequestDto requestDto);

    void save(BankCardModel bankCard);
}
