package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/activity")
public class ActivitiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private HomeService homeService;

    @RequestMapping(path = "/{item:^recruit|birth-month|rank-list-app|share-reward|app-download|landing-page$}", method = RequestMethod.GET)
    public ModelAndView activities(HttpServletRequest httpServletRequest, @PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/activities/" + item, "responsive", true);
        String loginName = httpServletRequest.getParameter("loginName");

        if (!Strings.isNullOrEmpty(loginName) && userService.loginNameIsExist(loginName.trim())) {
            modelAndView.addObject("referrer", loginName.trim());
        }

        return modelAndView;
    }

    @RequestMapping(path = "/landing-page", method = RequestMethod.GET)
    public ModelAndView landing() {
        ModelAndView modelAndView = new ModelAndView("/activities/landing-page", "responsive", true);

        modelAndView.addObject("loans", homeService.getLoans());

        return modelAndView;
    }
}
