package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawServiceImpl implements WithdrawService{

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Override
    public BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto) {
        String loginName = LoginUserInfo.getLoginName();
        withdrawDto.setLoginName(loginName);
        return payWrapperClient.withdraw(withdrawDto);
    }

    @Override
    public long sumSuccessWithdrawAmount(String loginName) {
        return withdrawMapper.findSumSuccessWithdrawByLoginName(loginName);
    }

}
