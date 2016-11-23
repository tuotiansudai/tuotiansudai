package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppBankCardService {

    BaseResponseDto<BankCardResponseDto> bindBankCard(BankCardRequestDto requestDto);

    BaseResponseDto<BankCardResponseDto> openFastPay(BankCardRequestDto requestDto);

    BaseResponseDto queryStatus(BankCardRequestDto requestDto);

    BaseResponseDto<BankCardReplaceResponseDataDto> replaceBankCard(BankCardReplaceRequestDto requestDto);

    BaseResponseDto<BankCardIsReplacingResponseDto> isReplacing(BaseParamDto baseParamDto);

}
