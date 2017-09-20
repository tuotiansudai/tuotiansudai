package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.AmountTransferException;

import java.util.Map;

public interface HuiZuActivateAccountService {

    BaseDto<PayDataDto> noPassword(HuiZuActivateAccountDto activateAccountDto);

    BaseDto<PayFormDataDto> password(HuiZuActivateAccountDto activateAccountDto);

    void postActivateAccount(long orderId) throws AmountTransferException;

    String activateAccountCallback(Map<String, String> paramsMap, String queryString);
}
