package com.tuotiansudai.console.service;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SystemRechargeService {
    @Autowired
    private PayWrapperClient payWrapperClient;

    public BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto systemRechargeDto) {
        return payWrapperClient.systemRecharge(systemRechargeDto);
    }
}
