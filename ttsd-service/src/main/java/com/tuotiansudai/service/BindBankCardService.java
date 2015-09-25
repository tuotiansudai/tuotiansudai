package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;

public interface BindBankCardService {

    BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto);

    String getUserName();
}
