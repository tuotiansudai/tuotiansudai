package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public class CarnivalActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @RequestMapping(value = "/single-carnival",method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/double-eleven", "responsive", true);
        modelAndView.addObject("myCount", 0);
        modelAndView.addObject("now", DateTime.now().toString("yyyy/MM/dd HH:mm:ss"));
        modelAndView.addObject("today", DateTime.now().toString("MM-dd"));
        modelAndView.addObject("steps", lotteryDrawActivityService.generateSteps(loginName));
        modelAndView.addObject("activityEnd", lotteryDrawActivityService.getActivityEndTime(ActivityCategory.CARNIVAL_ACTIVITY));
        return modelAndView;
    }
}