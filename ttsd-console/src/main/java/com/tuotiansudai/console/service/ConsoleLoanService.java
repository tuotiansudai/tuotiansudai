package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;

public interface ConsoleLoanService {

    BaseDto<PayDataDto> editLoan(LoanDto loanDto);
    BaseDto<PayDataDto> firstTrialPassed(LoanDto loanDto);
    BaseDto<PayDataDto> firstTrialRefused(LoanDto loanDto);
}
