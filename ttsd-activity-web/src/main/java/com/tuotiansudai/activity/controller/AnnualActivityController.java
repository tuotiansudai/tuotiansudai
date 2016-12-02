package com.tuotiansudai.activity.controller;


import com.google.common.base.Strings;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity/annual")
public class AnnualActivityController {


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/mid-autumn", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("loginName", Strings.isNullOrEmpty(loginName) ? "" : loginName);
        return modelAndView;
    }
}
