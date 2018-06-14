package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/account-anonymous")
public class AccountAnonymousController {


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView anonymousAccount() {
        return new ModelAndView("/account-anonymous");
    }
}
