package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppAutoInvestPlanService {
    BaseResponseDto buildAutoInvestPlan(AutoInvestPlanRequestDto autoInvestPlanRequestDto);
}
