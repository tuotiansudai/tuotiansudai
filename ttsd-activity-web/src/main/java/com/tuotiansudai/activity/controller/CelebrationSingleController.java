package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.CelebrationSingleService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/single")
public class CelebrationSingleController {


    @Autowired
    private CelebrationSingleService celebrationSingleService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView celebrationHeroRanking(){
        ModelAndView modelAndView=new ModelAndView("/single","responsive",true);
        String loginName = LoginUserInfo.getLoginName();

        modelAndView.addObject("drewChance", celebrationSingleService.findUserDrewChange(loginName));
        return modelAndView;

    }

}
