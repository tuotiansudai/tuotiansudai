package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.List;
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

    String cancelPayBackCallback(Map<String, String> paramsMap, String queryString);

    boolean processNotifyForLoanOut(long loanId);

    String loanOutCallback(Map<String, String> paramsMap, String queryString);

    boolean createAnxinContractJob(long loanId);
}
