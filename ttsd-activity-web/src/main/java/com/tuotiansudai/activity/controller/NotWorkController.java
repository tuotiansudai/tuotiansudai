package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.NotWorkService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity/not-work")
public class NotWorkController {
    @Autowired
    NotWorkService notWorkService;

    public ModelAndView getHome() {
        ModelAndView modelAndView = new ModelAndView("no-work");
        String loginName = LoginUserInfo.getLoginName();
        if (!Strings.isNullOrEmpty(loginName)) {
            modelAndView.addObject("investAmount", AmountConverter.convertCentToString(notWorkService.getUsersActivityInvestAmount(loginName)));
            modelAndView.addObject("needInvestAmount", AmountConverter.convertCentToString(notWorkService.getUsersNeedInvestAmount(loginName)));
        }
        return modelAndView;
    }
}
