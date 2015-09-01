package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestSource;

import java.util.List;

public interface InvestService {

    /**
     * 进行一次投资
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto);

    /**
     * 获取标的的已投资总额
     * @param loanId
     * @return
     */
    long getSuccessInvestedAmountByLoanId(long loanId);

    /**
     * 分页查询标的的投资列表
     * @param loanId
     * @param rowLimit
     * @param rowIndex
     * @return
     */
    List<InvestModel> getByLoanId(long loanId, int rowLimit, int rowIndex);
}
