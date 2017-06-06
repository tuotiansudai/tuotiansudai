package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.CelebrationOnePenService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "activity/one-pen")
public class CelebrationOnePenController {


    @Autowired
    private CelebrationOnePenService celebrationOnePenService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView celebrationHeroRanking(){
        ModelAndView modelAndView=new ModelAndView("one-pen","responsive",true);
        String loginName = LoginUserInfo.getLoginName();

        int drewChance=celebrationOnePenService.findUserDrewChange(loginName);

        return modelAndView;

    }

}
