package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.ShareCallbackRequestDataDto;
import com.tuotiansudai.api.service.MobileAppShareCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppShareCallbackController extends MobileAppBaseController {

    @Autowired
    private MobileAppShareCallbackService mobileAppShareCallbackService;

    @RequestMapping(value = "/share-callback", method = RequestMethod.POST)
    public BaseResponseDto shareCallback(@RequestBody ShareCallbackRequestDataDto requestDto) {
        mobileAppShareCallbackService.shareBannerSuccess(getLoginName(), requestDto);

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }

}
