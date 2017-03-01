package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
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
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private SignInService signInService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView national() {
        ModelAndView modelAndView = new ModelAndView("/activities/spring-festival", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("taskProgress", springFestivalActivityService.getTaskProgress(loginName));
        modelAndView.addObject("signedIn", signInService.signInIsSuccess(loginName));
        modelAndView.addObject("isDraw", lotteryDrawActivityService.toDayIsDrawByMobile(LoginUserInfo.getMobile(), ActivityCategory.SPRING_FESTIVAL_ACTIVITY) > 0 ? true : false);
        modelAndView.addObject("isActivity", springFestivalActivityService.isActivityTime());
        modelAndView.addObject("loginName", loginName);
        return modelAndView;
    }
}
