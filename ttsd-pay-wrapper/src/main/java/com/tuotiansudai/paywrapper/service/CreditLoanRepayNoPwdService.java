package com.tuotiansudai.paywrapper.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;

public interface CreditLoanRepayNoPwdService {

    BaseDto<PayDataDto> creditLoanRepayNoPwd(long orderId, String mobile, long amount);

}
