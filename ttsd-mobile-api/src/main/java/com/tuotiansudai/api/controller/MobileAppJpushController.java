package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.JpushRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppJpushService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.MessageFormat;

@RestController
public class MobileAppJpushController extends MobileAppBaseController {
    static Logger log = Logger.getLogger(MobileAppJpushController.class);

    @Autowired
    private MobileAppJpushService mobileAppJpushService;

    @RequestMapping(value = "/jpush", method = RequestMethod.POST)
    public BaseResponseDto storeJpushId(@Valid @RequestBody JpushRequestDto jpushRequestDto,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            log.debug(MessageFormat.format("{0}:{1}",errorCode,errorMessage));
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            jpushRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppJpushService.storeJpushId(jpushRequestDto);
        }

    }

}
