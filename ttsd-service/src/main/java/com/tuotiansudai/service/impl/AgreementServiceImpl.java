package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.service.AgreementService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/9/15.
 */
@Service
public class AgreementServiceImpl implements AgreementService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseDto<PayFormDataDto> agreement(AgreementDto agreementDto) {
        agreementDto.setLoginName(LoginUserInfo.getLoginName());
        BaseDto<PayFormDataDto> baseDto = payWrapperClient.agreement(agreementDto);
        return baseDto;
    }
}
