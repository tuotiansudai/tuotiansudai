package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private PayWrapperClient payWrapperClient;


    @Override
    public BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto) {
        String loginName = LoginUserInfo.getLoginName();
        rechargeDto.setLoginName(loginName);
        return payWrapperClient.recharge(rechargeDto);
    }
}
