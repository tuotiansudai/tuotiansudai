package com.tuotiansudai.service;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;

/**
 * Created by Administrator on 2015/9/15.
 */
public interface AgreementService {

    BaseDto<PayFormDataDto> agreement(String loginName, AgreementDto agreementDto);

}
