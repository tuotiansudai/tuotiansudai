package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;

public interface MobileAppAutoInvestPlanInfoService {
    BaseResponseDto getAutoInvestPlanInfoData(BaseParamDto baseParamDto);
}
