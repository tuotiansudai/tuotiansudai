package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppBankCardService;
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
        BankAsyncMessage bankAsyncMessage = bankBindCardService.bind(loginName, Source.MOBILE, ip, deviceId);
        if (bankAsyncMessage.isStatus()){
            BaseResponseDto<BankAsynResponseDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
            responseDto.setData(new BankAsynResponseDto(bankAsyncMessage.getUrl(), bankAsyncMessage.getData()));
            return responseDto;
        }
        return new BaseResponseDto<>(ReturnMessage.BIND_CARD_FAIL);
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> unBindBankCard(String loginName, String ip, String deviceId) {
        BankAsyncMessage bankAsyncMessage = bankBindCardService.unbind(loginName, Source.MOBILE, ip, deviceId);
        if (bankAsyncMessage.isStatus()){
            BaseResponseDto<BankAsynResponseDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
            responseDto.setData(new BankAsynResponseDto(bankAsyncMessage.getUrl(), bankAsyncMessage.getData()));
            return responseDto;
        }
        return new BaseResponseDto<>(ReturnMessage.UNBIND_CARD_FAIL);
    }
}
