package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;

public interface RepayService {

    BaseDto<PayFormDataDto> repay(RepayDto repayDto);

    BaseDto<LoanRepayDataDto> findLoanerLoanRepay(long loanId);

    BaseDto<InvestRepayDataDto> findInvestorInvestRepay(long investId);
}
