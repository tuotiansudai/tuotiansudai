package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;

import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


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

        modelAndView.addObject("messageList",  messageService.findMessageList(title, messageStatus, createdBy, MessageType.EVENT, index, pageSize));

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


}
