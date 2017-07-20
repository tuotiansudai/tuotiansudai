package com.tuotiansudai.service.impl;

import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentRedeemDto;
import com.tuotiansudai.dto.RedeemDataDto;
import com.tuotiansudai.dto.RedeemLimitsDataDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.CurrentRedeemService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentRedeemServiceImpl implements CurrentRedeemService {

    @Autowired
    private CurrentRestClient currentRestClient;

    @Override
    public BaseDto<RedeemDataDto> redeem(CurrentRedeemDto currentRedeemDto, String loginName) {
        return currentRestClient.redeem(convertToRedeemRequestDto(currentRedeemDto,loginName));
    }

    @Override
    public BaseDto<RedeemLimitsDataDto> limits(String loginName) {
        return currentRestClient.limits(loginName);

    }

    private RedeemRequestDto convertToRedeemRequestDto(CurrentRedeemDto currentRedeemDto, String loginName) {
        return new RedeemRequestDto(loginName, AmountConverter.convertStringToCent(currentRedeemDto.getAmount()), Source.WEB);
    }
}
