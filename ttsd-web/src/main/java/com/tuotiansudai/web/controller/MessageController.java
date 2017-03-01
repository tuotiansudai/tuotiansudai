package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.UserMessagePaginationItemDto;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;

@Controller
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private UserMessageService userMessageService;

    @RequestMapping(value = "/user-messages", method = RequestMethod.GET)
    public ModelAndView getMessages() {
        return new ModelAndView("/myAccount/user-message-list");
    }

    @RequestMapping(value = "/user-message-list-data", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getMessageListData(@RequestParam(value = "index", defaultValue = "1", required = false) int index) {

        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        BasePaginationDataDto<UserMessagePaginationItemDto> dataDto = userMessageService.getUserMessages(LoginUserInfo.getLoginName(), LoginUserInfo.getMobile(), index, 10);
        dto.setData(dataDto);

        return dto;
    }

    @RequestMapping(value = "/user-message/{userMessageId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView messageDetail(@PathVariable long userMessageId) {
        UserMessageModel userMessageModel = userMessageService.readMessage(userMessageId);
        if (userMessageModel == null || Strings.isNullOrEmpty(userMessageModel.getContent())) {
            return new ModelAndView("/error/404");
        }
        ModelAndView modelAndView = new ModelAndView("/myAccount/user-message-detail");
        modelAndView.addObject("title", userMessageModel.getTitle());
        modelAndView.addObject("content", userMessageModel.getContent());
        modelAndView.addObject("createdTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userMessageModel.getCreatedTime()));
        modelAndView.addObject("webUrl", userMessageService.getMessageWebURL(userMessageId));
        return modelAndView;
    }

    @RequestMapping(value = "/read-all", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> readAll() {
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userMessageService.readAll(LoginUserInfo.getLoginName(), MessageChannel.WEBSITE));
        dto.setData(dataDto);
        return dto;
    }
}
