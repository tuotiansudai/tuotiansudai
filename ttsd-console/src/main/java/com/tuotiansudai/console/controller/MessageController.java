package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageDto;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.service.MessageService;
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

        ModelAndView modelAndView = new ModelAndView("/manual-message-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("title", title);
        modelAndView.addObject("createdBy", createdBy);
        modelAndView.addObject("messageStatusInput", messageStatus);

        modelAndView.addObject("messageList", messageService.findMessageList(title, messageStatus, createdBy, MessageType.MANUAL, index, pageSize));

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
        ModelAndView modelAndView = new ModelAndView("/auto-message-list");

        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("title", title);
        modelAndView.addObject("createdBy", createdBy);
        modelAndView.addObject("messageStatusInput", messageStatus);

        modelAndView.addObject("messageList", messageService.findMessageList(title, messageStatus, createdBy, MessageType.EVENT, index, pageSize));

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

    @RequestMapping(value = "/manual-message/create", method = RequestMethod.GET)
    public ModelAndView createManualMessage() {
        return new ModelAndView("/manual-message-edit-view");
    }

    @RequestMapping(value = "/manual-message/create", method = RequestMethod.POST)
    public void createManualMessage(@RequestParam(value = "messageDto", required = true) MessageDto messageDto,
                                    @RequestParam(value = "importUsersId") long importUsersId) {
        messageService.createAndEditManualMessage(messageDto, importUsersId);
    }

    @RequestMapping(value = "/manual-message/{messageId}/edit", method = RequestMethod.GET)
    public ModelAndView editManualMessage(@PathVariable long messageId) {
        ModelAndView modelAndView = new ModelAndView("/article-edit");
        MessageDto messageDto = messageService.getMessageByMessageId(messageId);
        modelAndView.addObject(messageDto);
        return modelAndView;
    }

    @RequestMapping(value = "/manual-message/import-users", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> importUsers(@RequestParam(value = "importUsersId") long oldImportUsersId,
                                            HttpServletRequest httpServletRequest) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        InputStream inputStream = null;
        if (multipartFile.getOriginalFilename().endsWith(".xls")) {
            return new BaseDto<>(new BaseDataDto(false, "上传失败!文件必须是xls格式"));
        }
        try {
            inputStream = multipartFile.getInputStream();
            messageService.createImportReceivers(oldImportUsersId, inputStream);
        } catch (IOException e) {
            return new BaseDto<>(new BaseDataDto(false, "上传失败!文件内容错误"));
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(value = "manual-message/{messageId}/approve", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> messageApprove(@PathVariable(value = "messageId") long messageId) {
        return messageService.approveManualMessage(messageId);
    }

    @RequestMapping(value = "manual-message/{messageId}/reject", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> messageReject(@PathVariable(value = "messageId") long messageId) {
        return messageService.rejectManualMessage(messageId);
    }

    @RequestMapping(value = "manual-message/{messageId}/delete", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> messageDelete(@PathVariable(value = "messageId") long messageId) {
        return messageService.deleteManualMessage(messageId);
    }

    @RequestMapping(value = "manual-message/{messageId}/getReceiversCount", method = RequestMethod.GET)
    public long getMessageReceiversCount(@PathVariable(value = "messageId") long messageId) {
        return messageService.getMessageReceiverCount(messageId);
    }
}
