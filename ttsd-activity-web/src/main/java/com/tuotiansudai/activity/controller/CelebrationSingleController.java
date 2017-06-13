package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.CelebrationSingleActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/activity/single-rank")
public class CelebrationSingleController {

    @Autowired
    private CelebrationSingleActivityService celebrationSingleActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/single-rank", "responsive", true);
        modelAndView.addObject("drawCount", celebrationSingleActivityService.drawTimeByLoginNameAndActivityCategory(LoginUserInfo.getMobile(),LoginUserInfo.getLoginName()));
        return modelAndView;
    }



}
