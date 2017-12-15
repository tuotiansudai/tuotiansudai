package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.service.CashSnowballService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/cash-snowball")
public class CashSnowballController {
    @Autowired
    private CashSnowballService cashSnowballService;

    @Value(value = "${activity.cash.snowball.startTime}")
    private String activityCashSnowballStartTime;

    @Value(value = "${activity.cash.snowball.endTime}")
    private String activityCashSnowballEndTime;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView cashSnowBall() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/cash-snowball", "responsive", true);
        modelAndView.addObject("record", cashSnowballService.findAll());
        modelAndView.addAllObjects(cashSnowballService.userInvestAmount(LoginUserInfo.getLoginName()));
        modelAndView.addObject("activityStartTime", activityCashSnowballStartTime);
        modelAndView.addObject("activityEndTime", activityCashSnowballEndTime);
        modelAndView.addObject("currentTime", new DateTime().withTimeAtStartOfDay().toDate());
        return modelAndView;
    }
}
