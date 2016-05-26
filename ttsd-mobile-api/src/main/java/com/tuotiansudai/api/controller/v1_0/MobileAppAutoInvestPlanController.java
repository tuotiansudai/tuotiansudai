package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppAutoInvestPlanController extends MobileAppBaseController {
    @Autowired
    private MobileAppAutoInvestPlanService mobileAppAutoInvestPlanService;

    @RequestMapping(value = "/auto-invest-plan", method = RequestMethod.POST)
    public BaseResponseDto buildAutoInvestPlan(@Valid @RequestBody AutoInvestPlanRequestDto autoInvestPlanRequestDto, BindingResult bindingResult) {
        autoInvestPlanRequestDto.getBaseParam().setUserId(getLoginName());

        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            return mobileAppAutoInvestPlanService.buildAutoInvestPlan(autoInvestPlanRequestDto);

        }


    }

}
