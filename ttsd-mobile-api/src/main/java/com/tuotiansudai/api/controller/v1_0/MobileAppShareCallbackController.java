package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.ShareCallbackRequestDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppShareCallbackService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppShareCallbackController {

    @Autowired
    private MobileAppShareCallbackService mobileAppShareCallbackService;

    @RequestMapping(value = "/share-callback", method = RequestMethod.POST)
    public BaseResponseDto shareCallback(@RequestBody ShareCallbackRequestDataDto requestDto) {
        String loginName = LoginUserInfo.getLoginName();

        mobileAppShareCallbackService.shareBannerSuccess(loginName != null ? loginName : null, requestDto);

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
