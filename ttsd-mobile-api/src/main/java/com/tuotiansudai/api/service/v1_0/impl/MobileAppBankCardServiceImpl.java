package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBankCardService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BankBindCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppBankCardServiceImpl implements MobileAppBankCardService {

    private BankBindCardService bankBindCardService;

    @Autowired
    public MobileAppBankCardServiceImpl(BankBindCardService bankBindCardService){
        this.bankBindCardService = bankBindCardService;
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> bindBankCard(String loginName, String ip, String deviceId) {
        BankAsyncMessage bankAsyncMessage = bankBindCardService.bind(loginName, Source.MOBILE, ip, deviceId, Role.INVESTOR);
        return CommonUtils.mapToFormData(bankAsyncMessage);
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> unBindBankCard(String loginName, String ip, String deviceId) {
        BankAsyncMessage bankAsyncMessage = bankBindCardService.unbind(loginName, Source.MOBILE, ip, deviceId, Role.INVESTOR);
        return CommonUtils.mapToFormData(bankAsyncMessage);
    }
}
