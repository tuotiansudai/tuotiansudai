package com.tuotiansudai.api.controller;

import com.google.common.base.Strings;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/message-center")
public class MobileAppMessageCenterController {

    @Autowired
    MobileAppUserMessageService mobileAppUserMessageService;

    @RequestMapping(value = "/userMessage/{userMessageId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto<UserMessageViewDto> getUserMessage(@PathVariable long userMessageId, HttpServletRequest request) {
        UserMessageViewDto userMessageViewDto = mobileAppUserMessageService.getUserMessageModelById(userMessageId);
        String ip = request.getLocalAddr();
        int port = request.getLocalPort();
        if(!Strings.isNullOrEmpty(userMessageViewDto.getAppUrl())) {
            userMessageViewDto.setAppUrl(MessageFormat.format("http://{0}:{1}{2}", ip, port, userMessageViewDto.getAppUrl()));
        }
        BaseResponseDto<UserMessageViewDto> baseResponseDto = new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());

        baseResponseDto.setData(userMessageViewDto);

        return baseResponseDto;
    }
}
