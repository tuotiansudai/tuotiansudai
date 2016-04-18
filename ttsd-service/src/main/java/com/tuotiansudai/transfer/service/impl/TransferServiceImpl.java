package com.tuotiansudai.transfer.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.transfer.service.TransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService{

    static Logger logger = Logger.getLogger(TransferServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseDto<PayFormDataDto> transferPurchase(InvestDto investDto) {
        return payWrapperClient.purchase(investDto);
    }

}
