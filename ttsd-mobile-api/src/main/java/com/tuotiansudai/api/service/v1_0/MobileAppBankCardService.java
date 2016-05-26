package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankCardReplaceRequestDto;
import com.tuotiansudai.api.dto.v1_0.BankCardRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppBankCardService {

    BaseResponseDto bindBankCard(BankCardRequestDto requestDto);

    BaseResponseDto openFastPay(BankCardRequestDto requestDto);

    BaseResponseDto queryStatus(BankCardRequestDto requestDto);

    BaseResponseDto replaceBankCard(BankCardReplaceRequestDto requestDto);

}
