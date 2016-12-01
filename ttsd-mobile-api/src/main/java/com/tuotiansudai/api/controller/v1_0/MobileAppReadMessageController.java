package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReadMessageRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "阅读消息")
public class MobileAppReadMessageController extends MobileAppBaseController {
    @Autowired
    private MobileAppUserMessageService mobileAppUserMessageService;

    @RequestMapping(value = "/read-message", method = RequestMethod.POST)
    @ApiOperation("阅读消息")
    public BaseResponseDto readMessage(@RequestBody ReadMessageRequestDto readMessageRequestDto) {
        return mobileAppUserMessageService.updateReadMessage(readMessageRequestDto.getUserMessageId());
    }
}
