package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CurrentRedeemLimitResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.CurrentRedeemRequestDto;
import com.tuotiansudai.api.dto.v1_0.CurrentRedeemResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentRedeemService;
import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;

public class MobileAppCurrentRedeemServiceImpl implements MobileAppCurrentRedeemService {

    @Autowired
    private CurrentRestClient currentRestClient;

    @Override
    public BaseResponseDto<CurrentRedeemResponseDataDto> redeem(CurrentRedeemRequestDto currentRedeemRequestDto, String loginName) {
        String source = currentRedeemRequestDto.getBaseParam().getPlatform().toUpperCase();
        RedeemRequestDto redeemRequestDto = new RedeemRequestDto(loginName, AmountConverter.convertStringToCent(currentRedeemRequestDto.getAmount()), Source.valueOf(source));
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        CurrentRedeemResponseDataDto currentRedeemResponseDataDto = new CurrentRedeemResponseDataDto();
        baseResponseDto.setData(currentRedeemResponseDataDto);
        currentRedeemResponseDataDto.setAmount(currentRestClient.redeem(redeemRequestDto).getData().getAmount());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<CurrentRedeemLimitResponseDataDto> limitRedeem(String loginName) {

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        CurrentRedeemLimitResponseDataDto currentRedeemLimitResponseDataDto = new CurrentRedeemLimitResponseDataDto();
        currentRedeemLimitResponseDataDto.setRemainAmount(currentRestClient.limits(loginName).getData().getRemainAmount());
        currentRedeemLimitResponseDataDto.setTotalAmount(currentRestClient.limits(loginName).getData().getTotalAmount());
        baseResponseDto.setData(currentRedeemLimitResponseDataDto);
        return baseResponseDto;
    }


}
