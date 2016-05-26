package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BankCardRequestDto;
import com.tuotiansudai.api.dto.v1_0.BankCardResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MobileAppRechargeServiceImpl implements MobileAppRechargeService {
    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Override
    public BaseResponseDto recharge(BankCardRequestDto bankCardRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        RechargeDto rechargeDto = bankCardRequestDto.convertToRechargeDto();
        rechargeDto.setChannel(mobileAppChannelService.obtainChannelBySource(bankCardRequestDto.getBaseParam()));

        String loginName = rechargeDto.getLoginName();
        BankCardModel bankCardModel = bankCardMapper.findByLoginNameAndIsFastPayOn(loginName);
        if (bankCardModel == null) {
            return new BaseResponseDto(ReturnMessage.FAST_PAY_OFF.getCode(), ReturnMessage.FAST_PAY_OFF.getMsg());
        }
        rechargeDto.setBankCode(bankCardModel.getBankCode());
        BankCardResponseDto bankCardResponseDto = new BankCardResponseDto();
        try {
            BaseDto<PayFormDataDto> formDto = payWrapperClient.recharge(rechargeDto);
            if (formDto.getData().getStatus()) {
                bankCardResponseDto.setUrl(formDto.getData().getUrl());
                bankCardResponseDto.setRequestData(CommonUtils.mapToFormData(formDto.getData().getFields(), true));

            }
        } catch (UnsupportedEncodingException e) {
            return new BaseResponseDto(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode(), ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(bankCardResponseDto);

        return baseResponseDto;
    }
}
