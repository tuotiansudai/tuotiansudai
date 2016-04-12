package com.tuotiansudai.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class SystemAccountController {

    @RequestMapping(value = "/systemAccount", method = RequestMethod.GET)
    public ModelAndView systemAccount() {
        return new ModelAndView("/system-account");
    }
}
