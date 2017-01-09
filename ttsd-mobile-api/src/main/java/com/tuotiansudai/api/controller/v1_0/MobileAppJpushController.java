package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.JpushRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppJpushService;
import com.tuotiansudai.spring.LoginUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(description = "记录用户JPUSH ID")
public class MobileAppJpushController extends MobileAppBaseController {

    static Logger log = Logger.getLogger(MobileAppJpushController.class);

    @Autowired
    private MobileAppJpushService mobileAppJPushService;

    @RequestMapping(value = "/jpush", method = RequestMethod.POST)
    @ApiOperation("记录用户JPUSH ID")
    public BaseResponseDto storeJPushId(@Valid @RequestBody JpushRequestDto jPushRequestDto) {
        jPushRequestDto.getBaseParam().setUserId(LoginUserInfo.getLoginName());
        return mobileAppJPushService.storeJPushId(LoginUserInfo.getLoginName(), jPushRequestDto.getBaseParam().getPlatform(), jPushRequestDto.getJpushId());
    }

}
