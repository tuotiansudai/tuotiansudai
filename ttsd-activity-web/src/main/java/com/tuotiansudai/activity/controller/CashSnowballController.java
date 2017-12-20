package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.service.CashSnowballService;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.spring.LoginUserInfo;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/cash-snowball")
public class CashSnowballController {
    @Autowired
    private CashSnowballService cashSnowballService;

    private String activityCashSnowballStartTime = ETCDConfigReader.getReader().getValue("activity.cash.snowball.startTime");

    private DateTime activityCashSnowballEndTime = DateTime.parse(ETCDConfigReader.getReader().getValue("activity.cash.snowball.endTime"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView cashSnowBall() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/cash-snowball", "responsive", true);
        modelAndView.addObject("record", cashSnowballService.findAll());
        modelAndView.addAllObjects(cashSnowballService.userInvestAmount(LoginUserInfo.getLoginName()));
        modelAndView.addObject("activityStartTime", activityCashSnowballStartTime);
        modelAndView.addObject("activityEndTime", activityCashSnowballEndTime.minusDays(7).toString("yyyy-MM-dd HH:mm:ss"));
        return modelAndView;
    }
}
