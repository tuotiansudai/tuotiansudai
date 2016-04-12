package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgreementServiceImpl implements AgreementService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseDto<PayFormDataDto> agreement(String loginName, AgreementDto agreementDto) {
        agreementDto.setLoginName(loginName);
        return payWrapperClient.agreement(agreementDto);
    }
}
