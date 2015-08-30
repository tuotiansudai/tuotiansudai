package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;

public interface LoanService {

    BaseDto<PayFormDataDto> editLoan(LoanDto loanDto);
    BaseDto<PayFormDataDto> firstTrialPassed(LoanDto loanDto);
    BaseDto<PayFormDataDto> firstTrialRefused(LoanDto loanDto);
}
