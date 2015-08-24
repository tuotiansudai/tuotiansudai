package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.TitleModel;

import java.util.List;
import java.util.Map;

public interface LoanService {
    /**
     * @function 创建标的
     * @param loanDto
     * @return
     */
    BaseDto createLoan(LoanDto loanDto);

}
