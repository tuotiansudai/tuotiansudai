package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.CurrentWrapperClient;
import com.tuotiansudai.dto.CurrentRedeemDto;
import com.tuotiansudai.service.CurrentRedeemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentRedeemServiceImpl implements CurrentRedeemService {

    @Autowired
    private CurrentWrapperClient currentWrapperClient;

    @Override
    public void redeem(CurrentRedeemDto currentWithdrawDto, String loginName) {
        //调用封装好的CurrentWrapperClient

    }
}
