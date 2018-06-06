package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferSearchResponseModel;

import java.util.Map;

public interface UMPayRealTimeStatusService {

    Map<String, String> getUserStatus(String loginName);

    Map<String, String> getUserBalance(String loginName);

    Map<String, String> getPlatformStatus();

    Map<String, String> getLoanStatus(long loanId);

    BaseDto<PayDataDto> checkLoanAmount(long loanId, long investAmountSum);

    TransferSearchResponseModel getTransferStatus(String orderId, String merDate, String businessType);
}
