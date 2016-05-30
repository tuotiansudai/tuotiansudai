package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.JpushRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppJpushService;
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
    private MobileAppJpushService mobileAppJPushService;

    @RequestMapping(value = "/jpush", method = RequestMethod.POST)
    public BaseResponseDto storeJPushId(@Valid @RequestBody JpushRequestDto jPushRequestDto,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            log.debug(MessageFormat.format("{0}:{1}",errorCode,errorMessage));
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            jPushRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppJPushService.storeJPushId(jPushRequestDto);
        }

    }

}
