package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;

public interface BindBankCardService {

    BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto);

    String getUserName();

    BankCardModel getPassedBankCard();
}
