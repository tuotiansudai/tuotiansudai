package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MobileAppMessageController extends MobileAppBaseController {

    @Autowired
    private MobileAppUserMessageService mobileAppUserMessageService;

    @RequestMapping(value = "/get/messages", method = RequestMethod.POST)
    public BaseResponseDto getMessages(@RequestBody UserMessagesRequestDto requestDto) {
        return mobileAppUserMessageService.getUserMessages(requestDto);
    }

    @RequestMapping(value = "/get/unread-message-count")
    public BaseResponseDto getUnreadMessageCount(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppUserMessageService.getUnreadMessageCount(baseParamDto);
    }

    @RequestMapping(value = "/get/readAll", method = RequestMethod.POST)
    public BaseResponseDto readAll(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppUserMessageService.readAll(baseParamDto);
    }
}
