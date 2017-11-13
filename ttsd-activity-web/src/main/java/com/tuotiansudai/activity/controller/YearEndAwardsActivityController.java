package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/year-end-awards")
public class YearEndAwardsActivityController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView yearEndAwards(){
        return new ModelAndView("/activities/2017/year-award");
    }
}
