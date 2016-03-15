package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import org.springframework.transaction.annotation.Transactional;

public interface InvestTransferPurchaseService {

    BaseDto<PayFormDataDto> purchase(InvestDto investDto);

    void postPurchase(long investId) throws AmountTransferException;
}
