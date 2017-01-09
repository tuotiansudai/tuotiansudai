package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.SpringFestivalActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/spring-festival")
public class SpringFestivalActivityController {

    @Autowired
    private SpringFestivalActivityService springFestivalActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView national() {
        ModelAndView modelAndView = new ModelAndView("/activities/spring-festival", "responsive", true);
        modelAndView.addObject("taskProgress", springFestivalActivityService.getTaskProgress(LoginUserInfo.getLoginName()));
        return modelAndView;
    }
}
