package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppAgreementService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BankAccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppAgreementServiceImpl implements MobileAppAgreementService {

    static Logger logger = Logger.getLogger(MobileAppAgreementServiceImpl.class);

    private final BankAccountService bankAccountService;

    @Autowired
    public MobileAppAgreementServiceImpl(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> generateAgreementRequest(String loginName, String mobile, String ip, BaseParamDto baseParamDto) {
        BankAsyncMessage bankAsyncMessage = bankAccountService.authorization(Source.valueOf(baseParamDto.getBaseParam().getPlatform().toUpperCase()), loginName, mobile, ip, baseParamDto.getBaseParam().getDeviceId());

        return CommonUtils.mapToFormData(bankAsyncMessage);
    }

}
