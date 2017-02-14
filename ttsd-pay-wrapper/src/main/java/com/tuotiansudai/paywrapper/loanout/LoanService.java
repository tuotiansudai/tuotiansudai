package com.tuotiansudai.paywrapper.loanout;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.Map;

public interface LoanService {

    /**
     * @param loanId
     * @return
     * @function 创建标的
     */
    BaseDto<PayDataDto> createLoan(long loanId);

    /**
     * @param loanId
     * @param loanStatus
     * @return
     * @function 更新标的状态
     */
    BaseDto<PayDataDto> updateLoanStatus(long loanId, LoanStatus loanStatus);

    /**
     * 放款
     *
     * @param loanId
     * @return
     */
    BaseDto<PayDataDto> loanOut(long loanId);

    BaseDto<PayDataDto> cancelLoan(long loanId);

    String cancelPayBackCallback(Map<String, String> paramsMap, String queryString);

    String loanOutCallback(Map<String, String> paramsMap, String queryString);

}
