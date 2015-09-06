package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;

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
}
