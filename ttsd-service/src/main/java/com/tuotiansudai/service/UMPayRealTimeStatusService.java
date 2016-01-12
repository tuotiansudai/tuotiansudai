package com.tuotiansudai.service;

import java.util.Date;
import java.util.Map;

public interface UMPayRealTimeStatusService {
    Map<String, String> getUserStatus(String loginName);

    Map<String, String> getPlatformStatus();

    Map<String, String> getLoanStatus(long loanId);

    Map<String, String> getTransferStatus(String orderId, Date merDate, String businessType);
}
