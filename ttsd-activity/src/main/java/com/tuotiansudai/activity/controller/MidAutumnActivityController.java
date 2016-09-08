package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.service.MidAutumnActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value = "/activity/mid-autumn")
public class MidAutumnActivityController {

    @Autowired
    private MidAutumnActivityService midAutumnActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/hero-ranking", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("referrer", loginName);
        Map<String,Object> myFamilyMap = midAutumnActivityService.getMyMinAutumnActivityFamily(loginName);
        modelAndView.addObject("myFamilyNumber",myFamilyMap.get("number"));
        modelAndView.addObject("myFamilyGroup",myFamilyMap.get("myFamily"));
        modelAndView.addObject("myFamilyInvestAmount", myFamilyMap.get("myFamilyInvestAmount"));
        modelAndView.addObject("allFamilyInvestAmounts",midAutumnActivityService.getAllFamilyInvestAmount());
        return modelAndView;
    }
}
