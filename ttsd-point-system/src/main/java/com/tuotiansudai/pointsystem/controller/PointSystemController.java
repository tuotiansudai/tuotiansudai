package com.tuotiansudai.pointsystem.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/point-system")
public class PointSystemController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pointSystemHome() {
        ModelAndView modelAndView = new ModelAndView("/point/point-system");

       // String loginName = LoginUserInfo.getLoginName();

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

}
