package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.dto.BaseDto;

public interface AnxinSignService {

    BaseDto createContracts(long loanId);

    BaseDto createTransferContracts(long transferApplicationId);

    BaseDto updateContractResponse(long loanId,AnxinContractType anxinContractType);
}
