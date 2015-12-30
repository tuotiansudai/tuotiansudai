package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.service.SystemRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SystemRechargeServiceImpl implements SystemRechargeService {
    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto systemRechargeDto) {
        return payWrapperClient.systemRecharge(systemRechargeDto);
    }
}
