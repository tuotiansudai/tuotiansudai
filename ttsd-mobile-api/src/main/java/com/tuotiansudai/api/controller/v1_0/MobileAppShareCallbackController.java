package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.ShareCallbackRequestDataDto;
import com.tuotiansudai.api.service.MobileAppShareCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppShareCallbackController {

    @Autowired
    private MobileAppShareCallbackService mobileAppShareCallbackService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/share-callback", method = RequestMethod.POST)
    public BaseResponseDto shareCallback(@RequestBody ShareCallbackRequestDataDto requestDto) {
        Object loginName = httpServletRequest.getAttribute("currentLoginName");

        mobileAppShareCallbackService.shareBannerSuccess(loginName != null ? String.valueOf(loginName) : null, requestDto);

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
