package com.tuotiansudai.console.controller;

import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/current")
public class CurrentHomeController {

    @Value("${current.server}")
    private String currentServer;

    @RequestMapping(value = "")
    public ModelAndView currentHome() {

        String token = LoginUserInfo.getToken();
        return new ModelAndView("redirect:" + currentServer + "/console/index?token=" + token);
    }
}
