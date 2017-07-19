package com.tuotiansudai.service.impl;

import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.CurrentRedeemDto;
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
    public String redeem(CurrentRedeemDto currentRedeemDto, String loginName) {
        RedeemRequestDto redeemRequestDto = new RedeemRequestDto(loginName, AmountConverter.convertStringToCent(currentRedeemDto.getAmount()), Source.WEB);
        return String.valueOf(currentRestClient.redeem(redeemRequestDto).getData().getAmount());
    }
}
