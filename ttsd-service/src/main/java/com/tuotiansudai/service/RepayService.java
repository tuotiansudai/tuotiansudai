package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;

public interface RepayService {

    BaseDto<PayFormDataDto> repay(RepayDto repayDto);

    BaseDto<LoanerLoanRepayDataDto> getLoanRepay(String loginName, long loanId);

    void resetPayExpiredLoanRepay(long loanId);

    BaseDto<InvestRepayDataDto> findInvestorInvestRepay(String loginName, long investId);
}
