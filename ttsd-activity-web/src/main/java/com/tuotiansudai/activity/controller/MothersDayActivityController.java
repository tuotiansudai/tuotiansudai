package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.service.MothersDayActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity/mothers-day")
public class MothersDayActivityController {

    @Autowired
    private MothersDayActivityService mothersDayActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        ModelAndView modelAndView = new ModelAndView("/activities/mother-day", "responsive", true);
        modelAndView.addObject("investAmount", mothersDayActivityService.getInvestByLoginName(LoginUserInfo.getLoginName()));
        return modelAndView;
    }
}
