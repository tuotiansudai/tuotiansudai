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

@Controller
@RequestMapping(value = "/activity/carnival")
public class CarnivalActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadPageData() {
        ModelAndView modelAndView = new ModelAndView("/activities/integral-draw", "responsive", true);
        modelAndView.addObject("myCount", lotteryDrawActivityService.getDrawPrizeTime(LoginUserInfo.getMobile(), ActivityCategory.CARNIVAL_ACTIVITY));
        modelAndView.addObject("now", DateTime.now().toDate());
        return modelAndView;
    }

}
