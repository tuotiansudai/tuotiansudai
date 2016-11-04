package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserMessageDto;
import com.tuotiansudai.api.dto.v1_0.UserMessagesRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MobileAppMessageController extends MobileAppBaseController {

    @Autowired
    private MobileAppUserMessageService mobileAppUserMessageService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/get/messages", method = RequestMethod.POST)
    public BaseResponseDto getMessages(@RequestBody UserMessagesRequestDto requestDto) {
        return mobileAppUserMessageService.getUserMessages(requestDto);
    }

    @RequestMapping(value = "/get/unread-message-count")
    public BaseResponseDto getUnreadMessageCount(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppUserMessageService.getUnreadMessageCount(baseParamDto);
    }

    @RequestMapping(value = "/get/userMessage/{userMessageId}", method = RequestMethod.GET)
    public ModelAndView getUserMessage(@PathVariable long userMessageId) {
        ModelAndView modelAndView = new ModelAndView("");

        UserMessageDto userMessageDto = mobileAppUserMessageService.getUserMessageModelByIdAndLoginName(userMessageId, getLoginName());
        MessageCreateDto messageCreateDto = messageService.getMessageByMessageId(userMessageId);
        if (null != messageCreateDto && null != messageCreateDto.getAppUrl()) {
            modelAndView.addObject("appUrl", messageCreateDto.getAppUrl().getPath());
        }

        modelAndView.addObject("dto", userMessageDto);
        return modelAndView;
    }
}
