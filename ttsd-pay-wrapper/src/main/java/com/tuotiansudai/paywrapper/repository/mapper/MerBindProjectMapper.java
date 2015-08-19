package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;

public interface MerBindProjectMapper extends BaseSyncMapper{
    void createLoan(LoanModel loanModel);
}
