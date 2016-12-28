package com.tuotiansudai.console.service;

import com.tuotiansudai.client.PayWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ConsoleUMPayRealTimeStatusService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    public Map<String, String> getUserStatus(String loginName) {
        return payWrapperClient.getUserStatus(loginName);
    }

    public Map<String, String> getLoanStatus(long loanId) {
        return payWrapperClient.getLoanStatus(loanId);
    }

    public Map<String, String> getTransferStatus(String orderId, Date merDate, String businessType) {
        return payWrapperClient.getTransferStatus(orderId, merDate, businessType);
    }
}
