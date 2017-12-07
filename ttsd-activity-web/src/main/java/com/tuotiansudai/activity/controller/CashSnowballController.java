package com.tuotiansudai.activity.controller;


import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityModel;
import com.tuotiansudai.activity.service.CashSnowballService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity/cash-snowball")
public class CashSnowballController {

    @Autowired
    private CashSnowballService cashSnowballService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView cashSnowBall() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/cash-snowball", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("record", cashSnowballService.findAll());
//        modelAndView.addObject("annualizedAmount", cashSnowballActivity == null ? 0l : cashSnowballActivity.getAnnualizedAmount());
//        modelAndView.addObject("cashAmount", cashSnowballActivity == null ? 0l : cashSnowballActivity.getCashAmount());
//        modelAndView.addObject("")
        return modelAndView;
    }
}
