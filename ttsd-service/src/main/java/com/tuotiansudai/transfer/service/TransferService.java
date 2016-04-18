package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;

public interface TransferService {

    BaseDto<PayFormDataDto> transferPurchase(InvestDto investDto) throws InvestException;

}
