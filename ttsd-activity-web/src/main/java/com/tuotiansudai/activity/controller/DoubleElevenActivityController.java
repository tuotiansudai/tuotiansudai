package com.tuotiansudai.activity.controller;

import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/double-eleven")
public class DoubleElevenActivityController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doubleEleven() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/double-eleven", "responsive", true);
        modelAndView.addObject("aa","sss");
        return modelAndView;
    }

}

