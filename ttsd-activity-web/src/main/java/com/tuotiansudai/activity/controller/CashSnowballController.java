package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.service.CashSnowballService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value = "/activity/cash-snowball")
public class CashSnowballController {

    @Autowired
    private CashSnowballService cashSnowballService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView cashSnowBall() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/cash-snowball", "responsive", true);
        modelAndView.addObject("record", cashSnowballService.findAll());

        Map<String, String> maps = cashSnowballService.userInvestAmount(LoginUserInfo.getLoginName());
        modelAndView.addObject("annualizedAmount", maps.get("annualizedAmount"));
        modelAndView.addObject("cashAmount", maps.get("cashAmount"));
        modelAndView.addObject("nextAmount", maps.get("nextAmount"));
        return modelAndView;
    }
}
