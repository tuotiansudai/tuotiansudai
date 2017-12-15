package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.service.AnnualActivityService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


public class AnnualActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private AnnualActivityService annualActivityService;

    @Autowired
    private SignInService signInService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/new-year", "responsive", true);
        modelAndView.addObject("loginName", LoginUserInfo.getLoginName());
        modelAndView.addObject("time", 0);
        Map<String, String> investAmountTaskMap = annualActivityService.getInvestAmountTask(LoginUserInfo.getLoginName());
        modelAndView.addObject("investAmount", investAmountTaskMap.get("investAmount"));
        modelAndView.addObject("nextAmount", investAmountTaskMap.get("nextAmount"));
        modelAndView.addObject("signedIn", signInService.signInIsSuccess(LoginUserInfo.getLoginName()));
        modelAndView.addObject("task", annualActivityService.getTaskProgress(investAmountTaskMap.get("investAmount")));
        modelAndView.addObject("inActivityDate", investAmountTaskMap.get("inActivityDate"));
        return modelAndView;
    }
}
