package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;

public interface RepayService {

    BaseDto<PayFormDataDto> repay(RepayDto repayDto);

    BaseDto<LoanRepayDataDto> getLoanRepay(String loginName, long loanId);

    BaseDto<InvestRepayDataDto> findInvestorInvestRepay(String loginName, long investId);
}
