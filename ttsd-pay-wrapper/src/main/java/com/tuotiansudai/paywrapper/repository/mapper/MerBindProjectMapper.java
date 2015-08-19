package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.repository.model.LoanModel;

public interface MerBindProjectMapper extends BaseMapper{
    void createLoan(LoanModel loanModel);
}
