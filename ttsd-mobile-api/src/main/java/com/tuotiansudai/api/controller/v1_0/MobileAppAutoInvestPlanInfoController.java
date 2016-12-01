package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanInfoResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "获取用户自动投标计划")
public class MobileAppAutoInvestPlanInfoController extends MobileAppBaseController {
    @Autowired
    private MobileAppAutoInvestPlanInfoService mobileAppAutoInvestPlanInfoService;

    @RequestMapping(value = "/get/auto-invest-plan", method = RequestMethod.POST)
    @ApiOperation("开通自动投标签约")
    public BaseResponseDto<AutoInvestPlanInfoResponseDataDto> getAutoInvestPlanInfoData(@Valid @RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());

        return mobileAppAutoInvestPlanInfoService.getAutoInvestPlanInfoData(baseParamDto);

    }

}
