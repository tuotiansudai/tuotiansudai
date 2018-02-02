package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/start-work")
public class StartWorkActivityController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView startWork(){
        return new ModelAndView("/activities/2018/start-work");
    }

    @RequestMapping(value = "/wechat", method = RequestMethod.GET)
    public ModelAndView startWorkWechat(){
        return new ModelAndView("/wechat/start-work");
    }
}
