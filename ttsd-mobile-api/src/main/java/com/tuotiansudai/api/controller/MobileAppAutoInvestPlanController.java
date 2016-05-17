package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppAutoInvestPlanService;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class MobileAppAutoInvestPlanController extends MobileAppBaseController {
    @Autowired
    private MobileAppAutoInvestPlanService mobileAppAutoInvestPlanService;

    @RequestMapping(value = "/auto-invest-plan", method = RequestMethod.POST)
    public BaseResponseDto buildAutoInvestPlan(@Valid @RequestBody AutoInvestPlanRequestDto autoInvestPlanRequestDto, BindingResult bindingResult, HttpServletRequest request) {
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
