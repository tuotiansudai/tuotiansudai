package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleMessageService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.dto.MessagePaginationItemDto;
import com.tuotiansudai.message.repository.model.MessageCategory;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;


@Controller
@RequestMapping(value = "/message-manage")
public class ConsoleMessageController {

    @Autowired
    private ConsoleMessageService consoleMessageService;

    @RequestMapping(value = "/manual-message-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView manualMessageList(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                          @RequestParam(value = "messageCategory", required = false) MessageCategory messageCategory,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "updatedBy", required = false) String updatedBy,
                                          @RequestParam(value = "messageStatus", required = false) MessageStatus messageStatus) {
        final int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/message-manual-list");

        BasePaginationDataDto<MessagePaginationItemDto> dto = consoleMessageService.findMessagePagination(title, messageStatus, updatedBy, MessageType.MANUAL, messageCategory, index, pageSize);
        modelAndView.addObject("dto", dto);

        modelAndView.addObject("title", title);
        modelAndView.addObject("updatedBy", updatedBy);
        modelAndView.addObject("selectedMessageStatus", messageStatus);
        modelAndView.addObject("messageStatuses", MessageStatus.values());
        modelAndView.addObject("selectedMessageCategory", messageCategory);
        modelAndView.addObject("messageCategories", MessageCategory.values());

        return modelAndView;
    }

    @RequestMapping(value = "/auto-message-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView autoMessageList(@RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        final int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/message-auto-list");
        BasePaginationDataDto<MessagePaginationItemDto> dto = consoleMessageService.findMessagePagination(null, null, null, MessageType.EVENT, null, index, pageSize);
        modelAndView.addObject("dto", dto);

        return modelAndView;
    }

    @RequestMapping(value = "/manual-message", method = RequestMethod.GET)
    public ModelAndView manualMessageCreate() {
        ModelAndView modelAndView = new ModelAndView("/message-manual-edit");
        modelAndView.addObject("userGroups", Lists.newArrayList(MessageUserGroup.values()));
        modelAndView.addObject("selectedUserGroup", MessageUserGroup.ALL_USER);
        modelAndView.addObject("channelTypes", MessageChannel.values());
        modelAndView.addObject("selectedChannelTypes", Lists.newArrayList(MessageChannel.values()));
        modelAndView.addObject("messageCategories", Lists.newArrayList(MessageCategory.SYSTEM, MessageCategory.ACTIVITY));
        modelAndView.addObject("appUrls", Lists.newArrayList(AppUrl.values()));
        modelAndView.addObject("pushTypes", PushType.values());
        modelAndView.addObject("pushSources", PushSource.values());
        return modelAndView;
    }

    @RequestMapping(value = "/manual-message", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> createManualMessage(@RequestBody MessageCreateDto messageCreateDto) {
        Long messageId = consoleMessageService.createOrUpdateManualMessage(LoginUserInfo.getLoginName(), messageCreateDto);
        if (null == messageId) {
            return new BaseDto<>(new BaseDataDto(false));
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(value = "/manual-message/{messageId}/edit", method = RequestMethod.GET)
    public ModelAndView createManualMessage(@PathVariable long messageId) {
        ModelAndView modelAndView = new ModelAndView("/message-manual-edit");
        MessageCreateDto messageCreateDto = consoleMessageService.getEditMessage(messageId);

        modelAndView.addObject("dto", messageCreateDto);
        modelAndView.addObject("userGroups", Lists.newArrayList(MessageUserGroup.values()));
        modelAndView.addObject("selectedUserGroup", messageCreateDto.getUserGroup());
        modelAndView.addObject("channelTypes", Lists.newArrayList(MessageChannel.values()));
        modelAndView.addObject("selectedChannelTypes", messageCreateDto.getChannels());
        modelAndView.addObject("messageCategories", Lists.newArrayList(MessageCategory.SYSTEM, MessageCategory.ACTIVITY));
        modelAndView.addObject("appUrls",  Lists.newArrayList(AppUrl.values()));
        modelAndView.addObject("pushTypes", PushType.values());
        modelAndView.addObject("pushSources", PushSource.values());

        return modelAndView;
    }

    @RequestMapping(value = "/manual-message/import-users", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> importUsers(HttpServletRequest httpServletRequest) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            return new BaseDto<>(new BaseDataDto(false, "上传失败!文件必须是csv格式"));
        }
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            return new BaseDto<>(new BaseDataDto(true, String.valueOf(consoleMessageService.createImportUsers(inputStream))));
        } catch (IOException e) {
            return new BaseDto<>(new BaseDataDto(false, "上传失败!文件内容错误"));
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    @RequestMapping(value = "/approve/{messageId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> manualMessageApprove(@PathVariable long messageId) {
        return consoleMessageService.approveMessage(messageId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/reject/{messageId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> manualMessageReject(@PathVariable long messageId) {
        return consoleMessageService.rejectMessage(messageId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/delete/{messageId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> manualMessageDelete(@PathVariable long messageId) {
        return consoleMessageService.deleteMessage(messageId, LoginUserInfo.getLoginName());
    }
}
