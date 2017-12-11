package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/activity/cash-snowball")
public class CashSnowballActivityController {

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView zeroShopping(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/cash-snowball", "responsive", true);
        return modelAndView;
    }
}
