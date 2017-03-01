package com.tuotiansudai.web.controller;

import com.tuotiansudai.point.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/point")
public class PointController {

    @Autowired
    private SignInService signInService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView myPoint() {
        //重定向到新的积分商城
        return new ModelAndView("redirect:/point-shop");
    }
}
