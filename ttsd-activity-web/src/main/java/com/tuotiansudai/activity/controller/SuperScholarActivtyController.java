package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/super-scholar")
public class SuperScholarActivtyController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView activityHome(){
        return new ModelAndView("/activities/2018/super-scholar");
    }
}
