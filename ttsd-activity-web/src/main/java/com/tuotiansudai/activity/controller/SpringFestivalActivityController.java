package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.SpringFestivalActivityService;
import com.tuotiansudai.point.service.SignInService;
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

    @Autowired
    private SignInService signInService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView national() {
        ModelAndView modelAndView = new ModelAndView("/activities/spring-festival", "responsive", true);
        modelAndView.addObject("taskProgress", springFestivalActivityService.getTaskProgress(LoginUserInfo.getLoginName()));
        modelAndView.addObject("signedIn", signInService.signInIsSuccess(LoginUserInfo.getLoginName()));
        modelAndView.addObject("loginName", LoginUserInfo.getLoginName());
        return modelAndView;
    }
}
