package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ExchangeRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppExchangeController extends MobileAppBaseController{

    @Autowired
    private MobileAppExchangeService mobileAppExchangeService;

    @RequestMapping(value = "/get/code-exchange", method = RequestMethod.POST)
    public BaseResponseDto exchange(@Valid @RequestBody ExchangeRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            String errorCode = fieldError.getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        }
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppExchangeService.exchange(requestDto);
    }

}
