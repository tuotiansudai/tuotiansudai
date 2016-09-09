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

@Controller
@RequestMapping(value = "/activity/mid-autumn")
public class MidAutumnActivityController {

    @Autowired
    private MidAutumnActivityService midAutumnActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/mid-autumn", "responsive", true);
//        String loginName = LoginUserInfo.getLoginName();
        String loginName = "boss";
        modelAndView.addObject("loginName", Strings.isNullOrEmpty(loginName) ? "" : loginName);
        Map<String,Object> myFamilyMap = midAutumnActivityService.getMyMinAutumnActivityFamily(loginName);
        modelAndView.addObject("myFamilyNumber",myFamilyMap.get("number") == null ? "" : myFamilyMap.get("number"));
        modelAndView.addObject("myFamilyGroup",myFamilyMap.get("myFamily"));
        modelAndView.addObject("myFamilyInvestAmount", myFamilyMap.get("myFamilyInvestAmount")  == null ? "0" : myFamilyMap.get("myFamilyInvestAmount"));
        modelAndView.addObject("allFamilyInvestAmounts",midAutumnActivityService.getAllFamilyInvestAmount());
        return modelAndView;
    }
}
