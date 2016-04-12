package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.service.UMPayRealTimeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class UMPayRealTimeStatusServiceImpl implements UMPayRealTimeStatusService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public Map<String, String> getUserStatus(String loginName) {
        return payWrapperClient.getUserStatus(loginName);
    }

    @Override
    public Map<String, String> getPlatformStatus() {
        return payWrapperClient.getPlatformStatus();
    }

    @Override
    public Map<String, String> getLoanStatus(long loanId) {
        return payWrapperClient.getLoanStatus(loanId);
    }

    @Override
    public Map<String, String> getTransferStatus(String orderId, Date merDate, String businessType) {
        return payWrapperClient.getTransferStatus(orderId, merDate, businessType);
    }
}
