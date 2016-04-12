package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BankCardReplaceRequestDto;
import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppBankCardService {

    BaseResponseDto bindBankCard(BankCardRequestDto requestDto);

    BaseResponseDto openFastPay(BankCardRequestDto requestDto);

    BaseResponseDto queryStatus(BankCardRequestDto requestDto);

    BaseResponseDto replaceBankCard(BankCardReplaceRequestDto requestDto);

}
