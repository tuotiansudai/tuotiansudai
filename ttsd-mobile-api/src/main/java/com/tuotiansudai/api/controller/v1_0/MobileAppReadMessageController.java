package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReadMessageRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppReadMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppReadMessageController extends MobileAppBaseController {
    @Autowired
    private MobileAppReadMessageService mobileAppReadMessageService;

    @RequestMapping(value = "/read-message", method = RequestMethod.POST)
    public BaseResponseDto readMessage(@RequestBody ReadMessageRequestDto readMessageRequestDto) {
        return mobileAppReadMessageService.updateReadMessage(readMessageRequestDto.getUserMessageId());
    }
}
