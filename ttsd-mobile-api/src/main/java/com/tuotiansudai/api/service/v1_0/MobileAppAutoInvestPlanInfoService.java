package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanInfoResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppAutoInvestPlanInfoService {
    BaseResponseDto<AutoInvestPlanInfoResponseDataDto> getAutoInvestPlanInfoData(BaseParamDto baseParamDto);
}
