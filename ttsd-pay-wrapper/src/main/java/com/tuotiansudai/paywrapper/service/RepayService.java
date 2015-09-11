package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;

import java.util.List;

public interface RepayService {
    void generateInvestRepay(LoanModel loanModel, List<InvestModel> investModelList);
}
