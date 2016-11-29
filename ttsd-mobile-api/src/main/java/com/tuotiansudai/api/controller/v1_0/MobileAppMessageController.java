package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "站内信")
public class MobileAppMessageController extends MobileAppBaseController {

    @Autowired
    private MobileAppUserMessageService mobileAppUserMessageService;

    @RequestMapping(value = "/get/messages", method = RequestMethod.POST)
    @ApiOperation("消息列表")
    public BaseResponseDto<UserMessageResponseDataDto> getMessages(@RequestBody UserMessagesRequestDto requestDto) {
        return mobileAppUserMessageService.getUserMessages(requestDto);
    }

    @RequestMapping(value = "/get/unread-message-count")
    @ApiOperation("消息未读数")
    public BaseResponseDto<MobileAppUnreadMessageCount> getUnreadMessageCount(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppUserMessageService.getUnreadMessageCount(baseParamDto);
    }

    @RequestMapping(value = "/get/readAll", method = RequestMethod.POST)
    public BaseResponseDto readAll(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppUserMessageService.readAll(baseParamDto);
    }
}
