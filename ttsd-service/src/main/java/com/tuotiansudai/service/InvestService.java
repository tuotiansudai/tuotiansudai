package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestSource;

import java.util.List;

public interface InvestService {

    /**
     * 进行一次投资
     * @param loanId
     * @param amount
     * @param source
     */
    long doInvest(long loanId, long amount, InvestSource source);

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
