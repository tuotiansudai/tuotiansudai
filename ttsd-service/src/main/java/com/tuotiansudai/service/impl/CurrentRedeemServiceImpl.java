package com.tuotiansudai.service.impl;

import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentRedeemDto;
import com.tuotiansudai.dto.RedeemDataDto;
import com.tuotiansudai.dto.RedeemLimitsDataDto;
import com.tuotiansudai.service.CurrentRedeemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentRedeemServiceImpl implements CurrentRedeemService {

    @Autowired
    private CurrentRestClient currentRestClient;

    @Override
    public BaseDto<RedeemDataDto> redeem(CurrentRedeemDto currentRedeemDto) {
        return currentRestClient.redeem(convertToRedeemRequestDto(currentRedeemDto));
    }

    @Override
    public BaseDto<RedeemLimitsDataDto> limits(String loginName) {
        return currentRestClient.limits(loginName);

    }

    private RedeemRequestDto convertToRedeemRequestDto(CurrentRedeemDto currentRedeemDto){
        return new RedeemRequestDto(currentRedeemDto.getLoginName(), currentRedeemDto.getAmount(),currentRedeemDto.getSource());
    }
}
