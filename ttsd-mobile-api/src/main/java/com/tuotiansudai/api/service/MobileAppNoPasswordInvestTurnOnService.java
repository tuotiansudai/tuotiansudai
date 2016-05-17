package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppNoPasswordInvestTurnOnService {
    BaseResponseDto noPasswordInvestTurnOn(BaseParamDto baseParamDto, String ip);
}
