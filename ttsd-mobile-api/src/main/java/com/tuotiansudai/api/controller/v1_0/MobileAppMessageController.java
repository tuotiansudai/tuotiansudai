package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserMessagesRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppMessageController extends MobileAppBaseController {

    @Autowired
    private MobileAppUserMessageService mobileAppUserMessageService;

    @RequestMapping(value = "/get/messages", method = RequestMethod.POST)
    public BaseResponseDto getMessages(@RequestBody UserMessagesRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppUserMessageService.getUserMessages(requestDto);
    }

    @RequestMapping(value = "/get/unread-message-count")
    public BaseResponseDto getUnreadMessageCount(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppUserMessageService.getUnreadMessageCount(baseParamDto);
    }
}
