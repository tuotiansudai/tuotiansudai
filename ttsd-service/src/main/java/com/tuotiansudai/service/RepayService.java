package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.LoanModel;

/**
 * Created by Administrator on 2015/9/6.
 */
public interface RepayService {

    public void generateLoanRepay(LoanModel loanModel);

    public void generateInvestRepay(LoanModel loanModel);
}
