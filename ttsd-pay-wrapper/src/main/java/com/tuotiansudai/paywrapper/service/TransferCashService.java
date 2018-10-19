package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.activity.repository.dto.InviteHelpActivityPayCashDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;

public interface TransferCashService {

    BaseDto<PayDataDto> transferCash(TransferCashDto transferCashDto);
}
