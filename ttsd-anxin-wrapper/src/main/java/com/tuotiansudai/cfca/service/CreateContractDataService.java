package com.tuotiansudai.cfca.service;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import com.tuotiansudai.repository.model.InvestModel;

/**
 * Created by qduljs2011 on 2018/9/5.
 */
public interface CreateContractDataService {
    CreateContractVO createInvestorContractVo(long loanId, InvestModel investModel);

    String getSupportContractVersion();
}
