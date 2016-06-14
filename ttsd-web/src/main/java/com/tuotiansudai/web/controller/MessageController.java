package com.tuotiansudai.web.controller;

import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private UserMessageService userMessageService;

    @RequestMapping(value = "/user-messages", method = RequestMethod.GET)
    public ModelAndView getMessages(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ModelAndView("/user-message-list");
    }


    @RequestMapping(value = "user-message/{id:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView messageDetail(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/user-message");
//        String loginName = LoginUserInfo.getLoginName();
//        modelAndView.addObject("loginName",loginName);
//        modelAndView.addObject("messageDetail", userMessageService.findById(id));
        return modelAndView;
    }
}
