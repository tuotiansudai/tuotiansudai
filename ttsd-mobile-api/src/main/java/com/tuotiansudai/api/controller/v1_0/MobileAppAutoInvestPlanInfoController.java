package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppAutoInvestPlanInfoController extends MobileAppBaseController {
    @Autowired
    private MobileAppAutoInvestPlanInfoService mobileAppAutoInvestPlanInfoService;

    @RequestMapping(value = "/get/auto-invest-plan", method = RequestMethod.POST)
    public BaseResponseDto getAutoInvestPlanInfoData(@Valid @RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());

        return mobileAppAutoInvestPlanInfoService.getAutoInvestPlanInfoData(baseParamDto);

    }

}
