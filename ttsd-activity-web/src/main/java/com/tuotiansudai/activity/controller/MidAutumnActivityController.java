package com.tuotiansudai.activity.controller;


import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.MidAutumnActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


public class MidAutumnActivityController {

    @Autowired
    private MidAutumnActivityService midAutumnActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/mid-autumn", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("loginName", Strings.isNullOrEmpty(loginName) ? "" : loginName);
        Map<String,Object> myFamilyMap = midAutumnActivityService.getMidAutumnHomeData(loginName);
        modelAndView.addObject("myFamilyNumber",myFamilyMap.get("myFamilyNum"));
        modelAndView.addObject("myFamilyGroup",myFamilyMap.get("myFamily"));
        modelAndView.addObject("myFamilyInvestAmount", myFamilyMap.get("todayInvestAmount"));
        modelAndView.addObject("myFamilyTotalInvestAmount", myFamilyMap.get("totalInvestAmount"));
        modelAndView.addObject("allFamilyInvestAmounts",myFamilyMap.get("topThreeFamily"));
        modelAndView.addObject("isOverdue",myFamilyMap.get("isOverdue"));
        return modelAndView;
    }
}
