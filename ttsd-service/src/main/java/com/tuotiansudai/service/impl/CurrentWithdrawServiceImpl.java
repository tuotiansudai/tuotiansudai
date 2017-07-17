package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.CurrentWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentWithdrawDto;
import com.tuotiansudai.service.CurrentWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentWithdrawServiceImpl implements CurrentWithdrawService {

    @Autowired
    private CurrentWrapperClient currentWrapperClient;


    @Override
    public void currentWithdraw(CurrentWithdrawDto currentWithdrawDto, String loginName) {
        //调用封装好的CurrentWrapperClient

    }
}
