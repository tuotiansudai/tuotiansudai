package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.service.UMPayRealTimeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UMPayRealTimeStatusServiceImpl implements UMPayRealTimeStatusService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public Map<String, String> getPlatformStatus() {
        return payWrapperClient.getPlatformStatus();
    }
}
