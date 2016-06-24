package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppAutoInvestPlanService {
    BaseResponseDto buildAutoInvestPlan(AutoInvestPlanRequestDto autoInvestPlanRequestDto);
}
