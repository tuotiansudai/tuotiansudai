package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/national-background")
public class NationalBackground {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView nationalBachground(){
        return new ModelAndView("/activities/national-background");
    }
}
