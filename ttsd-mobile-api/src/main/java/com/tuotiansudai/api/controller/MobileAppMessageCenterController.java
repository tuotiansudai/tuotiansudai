package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserMessageViewDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/message-center")
public class MobileAppMessageCenterController {

    @Autowired
    MobileAppUserMessageService mobileAppUserMessageService;

    @RequestMapping(value = "/userMessage/{userMessageId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto<UserMessageViewDto> getUserMessage(@PathVariable long userMessageId) {
        UserMessageViewDto userMessageViewDto = mobileAppUserMessageService.getUserMessageModelById(userMessageId);
        BaseResponseDto<UserMessageViewDto> baseResponseDto = new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());

        baseResponseDto.setData(userMessageViewDto);

        return baseResponseDto;
    }
}
