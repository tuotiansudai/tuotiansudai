package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentRedeemService;
import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RedeemDataDto;
import com.tuotiansudai.dto.RedeemLimitsDataDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCurrentRedeemServiceImpl implements MobileAppCurrentRedeemService {

    @Autowired
    private CurrentRestClient currentRestClient;

    @Override
    public BaseResponseDto<CurrentRedeemResponseDataDto> redeem(CurrentRedeemRequestDto currentRedeemRequestDto, String loginName) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        CurrentRedeemResponseDataDto currentRedeemResponseDataDto = new CurrentRedeemResponseDataDto();

        BaseDto<RedeemLimitsDataDto> limitsBaseDto = currentRestClient.limits(loginName);
        RedeemRequestDto redeemRequestDto = convertToRedeemRequestDto(currentRedeemRequestDto,loginName);

        if(limitsBaseDto.getData().getRemainAmount() < redeemRequestDto.getAmount()){
            baseResponseDto.setCode(ReturnMessage.REDEEM_NO_AMOUNT.getCode());
            baseResponseDto.setMessage(ReturnMessage.REDEEM_NO_AMOUNT.getMsg());
            return baseResponseDto;
        }

        BaseDto<RedeemDataDto> baseDto = currentRestClient.redeem(redeemRequestDto);
        if(baseDto.isSuccess()){
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
            currentRedeemResponseDataDto.setAmount(baseDto.getData().getAmount());
            baseResponseDto.setData(currentRedeemResponseDataDto);
        }
        else{
            baseResponseDto.setCode(ReturnMessage.REDEEM_FAILED.getCode());
            baseResponseDto.setMessage(ReturnMessage.REDEEM_FAILED.getMsg());
        }
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<CurrentRedeemLimitResponseDataDto> limitRedeem(String loginName) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        CurrentRedeemLimitResponseDataDto currentRedeemLimitResponseDataDto = new CurrentRedeemLimitResponseDataDto();
        BaseDto<RedeemLimitsDataDto> baseDto = currentRestClient.limits(loginName);
        if (baseDto.isSuccess()){
            baseResponseDto.setCode("");
            baseResponseDto.setMessage("");
            currentRedeemLimitResponseDataDto.setRemainAmount(baseDto.getData().getRemainAmount());
            currentRedeemLimitResponseDataDto.setTotalAmount(baseDto.getData().getTotalAmount());
            baseResponseDto.setData(currentRedeemLimitResponseDataDto);
        }
        else{
            baseResponseDto.setCode(ReturnMessage.REDEEM_LIMITS_FAILED.getCode());
            baseResponseDto.setMessage(ReturnMessage.REDEEM_LIMITS_FAILED.getMsg());
        }
        return baseResponseDto;
    }

    private RedeemRequestDto convertToRedeemRequestDto(CurrentRedeemRequestDto currentRedeemRequestDto, String loginName) {
        String source = currentRedeemRequestDto.getBaseParam().getPlatform().toUpperCase();
        return new RedeemRequestDto(loginName, AmountConverter.convertStringToCent(currentRedeemRequestDto.getAmount()), Source.valueOf(source));
    }

}
