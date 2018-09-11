package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;

public interface RepayService {

    BankAsyncMessage normalRepay(RepayDto repayDto);

    BankAsyncMessage advancedRepay(RepayDto repayDto);

    BaseDto<LoanerLoanRepayDataDto> getLoanRepay(String loginName, long loanId);

    BaseDto<InvestRepayDataDto> findInvestorInvestRepay(String loginName, long investId);

    UmpAsyncMessage umpNormalRepay(RepayDto repayDto);

    UmpAsyncMessage umpAdvancedRepay(RepayDto repayDto);
}
