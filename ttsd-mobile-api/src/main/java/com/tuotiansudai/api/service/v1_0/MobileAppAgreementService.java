package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppAgreementService {

    BaseResponseDto<BankAsynResponseDto> generateAgreementRequest(String loginName, String mobile, String ip, BaseParamDto baseParamDto);

}
