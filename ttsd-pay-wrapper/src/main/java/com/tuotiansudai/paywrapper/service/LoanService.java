package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.LoanModel;

public interface LoanService {

    /**
     * @function 创建标的
     * @param loanDto
     * @return
     */
    BaseDto<PayDataDto> createLoan(LoanDto loanDto);
}
