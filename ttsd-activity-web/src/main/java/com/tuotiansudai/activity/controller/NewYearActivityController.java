package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/new-year")
public class NewYearActivityController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/activities/2018/new-year-increase-interest", "responsive", false);
        return modelAndView;
    }
}
