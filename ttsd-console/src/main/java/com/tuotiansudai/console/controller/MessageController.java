package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.dto.MessageCompleteDto;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.message.service.MessageService;
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
import java.util.List;


@Controller
@RequestMapping(value = "/message-manage")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/manual-message-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView manualMessageList(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "createdBy", required = false) String createdBy,
                                          @RequestParam(value = "messageStatus", required = false) MessageStatus messageStatus) {

        ModelAndView modelAndView = new ModelAndView("/message-manual-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("title", title);
        modelAndView.addObject("createdBy", createdBy);
        modelAndView.addObject("messageStatusInput", messageStatus);

        modelAndView.addObject("messageList", messageService.findMessageCompleteDtoList(title, messageStatus, createdBy, MessageType.MANUAL, index, pageSize));

        modelAndView.addObject("messageStatuses", Lists.newArrayList(MessageStatus.values()));
        long messageCount = messageService.findMessageCount(title, messageStatus, createdBy, MessageType.MANUAL);
        modelAndView.addObject("messageCount", messageCount);
        long totalPages = messageCount / pageSize + (messageCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);

        return modelAndView;
    }

    @RequestMapping(value = "/auto-message-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView autoMessageList(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                        @RequestParam(value = "title", required = false) String title,
                                        @RequestParam(value = "createdBy", required = false) String createdBy,
                                        @RequestParam(value = "messageStatus", required = false) MessageStatus messageStatus) {
        ModelAndView modelAndView = new ModelAndView("/message-auto-list");

        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("title", title);
        modelAndView.addObject("createdBy", createdBy);
        modelAndView.addObject("messageStatusInput", messageStatus);

        modelAndView.addObject("messageList", messageService.findMessageCompleteDtoList(title, messageStatus, createdBy, MessageType.EVENT, index, pageSize));

        modelAndView.addObject("messageStatuses", Lists.newArrayList(MessageStatus.values()));
        long messageCount = messageService.findMessageCount(title, messageStatus, createdBy, MessageType.EVENT);
        modelAndView.addObject("messageCount", messageCount);
        long totalPages = messageCount / pageSize + (messageCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);

        return modelAndView;
    }

    @RequestMapping(value = "/manual-message", method = RequestMethod.GET)
    public ModelAndView manualMessageCreate() {
        ModelAndView modelAndView = new ModelAndView("/message-manual-edit");
        modelAndView.addObject("userGroups", Lists.newArrayList(MessageUserGroup.values()));
        List<MessageUserGroup> selectedUserGroups = Lists.newArrayList(MessageUserGroup.values());
        selectedUserGroups.remove(MessageUserGroup.IMPORT_USER);
        modelAndView.addObject("selectedUserGroups", selectedUserGroups);
        modelAndView.addObject("channelTypes", Lists.newArrayList(MessageChannel.values()));
        modelAndView.addObject("selectedChannelTypes", Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
        modelAndView.addObject("manualMessageTypes", MessageCategory.values());
        modelAndView.addObject("appUrls", AppUrl.values());
        modelAndView.addObject("pushTypes", PushType.values());
        modelAndView.addObject("pushSources", PushSource.values());
        return modelAndView;
    }

    @RequestMapping(value = "/manual-message/{messageId}/edit", method = RequestMethod.GET)
    public ModelAndView createManualMessage(@PathVariable long messageId) {
        MessageCompleteDto messageCompleteDto = messageService.findMessageCompleteDtoByMessageId(messageId);

        ModelAndView modelAndView = new ModelAndView("/message-manual-edit");
        modelAndView.addObject("dto", messageCompleteDto);
        modelAndView.addObject("userGroups", Lists.newArrayList(MessageUserGroup.values()));
        modelAndView.addObject("selectedUserGroups", messageCompleteDto.getUserGroups());
        modelAndView.addObject("channelTypes", Lists.newArrayList(MessageChannel.values()));
        modelAndView.addObject("selectedChannelTypes", messageCompleteDto.getChannels());
        modelAndView.addObject("manualMessageTypes", MessageCategory.values());
        modelAndView.addObject("appUrls", AppUrl.values());
        modelAndView.addObject("pushTypes", PushType.values());
        modelAndView.addObject("pushSources", PushSource.values());

        return modelAndView;
    }

    @RequestMapping(value = "/manual-message/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> createManualMessage(@RequestBody MessageCompleteDto messageCompleteDto, @RequestParam(value = "importUsersId") long importUsersId) {
        messageCompleteDto.setUpdatedBy(LoginUserInfo.getLoginName());
        if (!messageService.isMessageExist(messageCompleteDto.getId())) {
            messageCompleteDto.setCreatedBy(LoginUserInfo.getLoginName());
        }
        messageService.createAndEditManualMessage(messageCompleteDto, importUsersId);
        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(value = "/manual-message/import-users/{importUsersId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> importUsers(@PathVariable long importUsersId,
                                            HttpServletRequest httpServletRequest) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        InputStream inputStream = null;
        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            return new BaseDto<>(new BaseDataDto(false, "上传失败!文件必须是csv格式"));
        }
        long newImportUsersId = importUsersId;
        try {
            inputStream = multipartFile.getInputStream();
            newImportUsersId = messageService.createImportReceivers(importUsersId, inputStream);
        } catch (IOException e) {
            return new BaseDto<>(new BaseDataDto(false, "上传失败!文件内容错误"));
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return new BaseDto<>(new BaseDataDto(true, String.valueOf(newImportUsersId)));
    }

    @RequestMapping(value = "/approve/{messageId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> manualMessageApprove(@PathVariable long messageId) {
        return messageService.approveMessage(messageId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/reject/{messageId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> manualMessageReject(@PathVariable long messageId) {
        return messageService.rejectMessage(messageId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/delete/{messageId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> manualMessageDelete(@PathVariable long messageId) {
        return messageService.deleteMessage(messageId, LoginUserInfo.getLoginName());
    }
}
