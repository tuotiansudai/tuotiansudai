package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;

public interface BindBankCardService {

    BankCardModel getPassedBankCard(String loginName);

    BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto);

    BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto);

    boolean isReplacing(String loginName);
}
