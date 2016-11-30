package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanDataDto;
import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanService;
import com.tuotiansudai.util.RequestIPParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(description = "获取用户自动投标计划")
public class MobileAppAutoInvestPlanController extends MobileAppBaseController {
    @Autowired
    private MobileAppAutoInvestPlanService mobileAppAutoInvestPlanService;


    @RequestMapping(value = "/auto-invest-plan", method = RequestMethod.POST)
    @ApiOperation("开通自动投标签约")
    public BaseResponseDto<AutoInvestPlanDataDto> buildAutoInvestPlan(@Valid @RequestBody AutoInvestPlanRequestDto autoInvestPlanRequestDto, BindingResult bindingResult, HttpServletRequest request) {
        autoInvestPlanRequestDto.getBaseParam().setUserId(getLoginName());
        autoInvestPlanRequestDto.setIp(RequestIPParser.parse(request));

        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            return mobileAppAutoInvestPlanService.buildAutoInvestPlan(autoInvestPlanRequestDto);

        }


    }

}
