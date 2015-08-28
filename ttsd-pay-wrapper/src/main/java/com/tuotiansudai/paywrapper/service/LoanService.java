package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;

public interface LoanService {
    BaseDto<PayDataDto> createLoan(LoanDto loanDto);
}
