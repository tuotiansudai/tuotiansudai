package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ump")
public class UmpHomeController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home(){
        return new ModelAndView("/ump-account");
    }
}
