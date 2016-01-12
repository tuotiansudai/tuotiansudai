package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.JpushRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppJpushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppJpushController extends MobileAppBaseController {

    @Autowired
    private MobileAppJpushService mobileAppJpushService;

    @RequestMapping(value = "/jpush", method = RequestMethod.POST)
    public BaseResponseDto storeJpushId(@Valid @RequestBody JpushRequestDto jpushRequestDto,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            return mobileAppJpushService.storeJpushId(jpushRequestDto);
        }

    }

}
