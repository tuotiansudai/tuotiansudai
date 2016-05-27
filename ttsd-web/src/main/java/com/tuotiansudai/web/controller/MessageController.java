package com.tuotiansudai.web.controller;

import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private UserMessageService userMessageService;

    @RequestMapping(value = "/{id:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView messageDetail(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/message");
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("messageDetail", userMessageService.findById(id));
        return modelAndView;
    }
}
