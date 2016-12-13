package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto) {
        return payWrapperClient.recharge(rechargeDto);
    }

    @Override
    public long sumSuccessRechargeAmount(String loginName) {
        return rechargeMapper.findSumSuccessRechargeByLoginName(loginName);
    }

    @Override
    public RechargeModel findRechargeById(long id){
        return rechargeMapper.findById(id);
    }
}
