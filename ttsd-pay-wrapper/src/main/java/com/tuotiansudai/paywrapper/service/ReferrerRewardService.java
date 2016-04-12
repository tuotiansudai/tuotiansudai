package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;

import java.util.List;

public interface ReferrerRewardService {

    void rewardReferrer(LoanModel loanModel, List<InvestModel> investModels);

}
