package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;

public interface AnxinSignService {

    BaseDto<BaseDataDto> createContracts(long loanId);

    BaseDto updateContractResponse(long loanId);
}
