package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.service.AnnualActivityService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity/annual")
public class AnnualActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private AnnualActivityService annualActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/mid-autumn", "responsive", true);
        modelAndView.addObject("loginName", LoginUserInfo.getLoginName());
        modelAndView.addObject("time", lotteryDrawActivityService.countDrawLotteryTime(LoginUserInfo.getMobile(), ActivityCategory.ANNUAL_ACTIVITY));
        modelAndView.addObject("investAmount", annualActivityService.successInvestAmount(LoginUserInfo.getLoginName()));
        return modelAndView;
    }
}
