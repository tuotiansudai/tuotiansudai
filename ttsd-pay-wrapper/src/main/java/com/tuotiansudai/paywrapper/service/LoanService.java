package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.Map;

public interface LoanService {

    /**
     * @function 创建标的
     * @param loanId
     * @return
     */
    BaseDto<PayDataDto> createLoan(long loanId);

    /**
     * @function 更新标的状态
     * @param loanId
     * @param loanStatus
     * @return
     */
    BaseDto<PayDataDto> updateLoanStatus(long loanId, LoanStatus loanStatus);

    /**
     * 放款
     * @param loanId
     * @return
     */
    BaseDto<PayDataDto> loanOut(long loanId);

    BaseDto<PayDataDto> cancelLoan(long loanId);

    boolean postLoanOut(long loanId);

    String cancelPayBackCallback(Map<String, String> paramsMap, String queryString);
}
