package com.tuotiansudai.service;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;

public interface AgreementService {

    BaseDto<PayFormDataDto> agreement(String loginName, AgreementDto agreementDto);

}
