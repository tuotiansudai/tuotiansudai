package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppBankCardService {

    BaseResponseDto<BankCardResponseDto> bindBankCard(BankCardRequestDto requestDto);
}
