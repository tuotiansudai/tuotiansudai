package com.tuotiansudai.api.controller.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailRequestDto;
import com.tuotiansudai.api.dto.v2_0.ReturnMessage;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppLoanDetailV2Controller extends MobileAppBaseController {

    @Autowired
    private MobileAppLoanDetailService mobileAppLoanDetailV2Service;

    @RequestMapping(value = "/get/loan", method = RequestMethod.POST)
    public BaseResponseDto queryLoanDetail(@Valid LoanDetailRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        }
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppLoanDetailV2Service.findLoanDetail(requestDto);
    }
}
