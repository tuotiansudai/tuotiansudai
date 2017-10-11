package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.ZeroShoppingActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/zero-shopping")
public class ZeroShoppingActivityController {

    @Autowired
    private ZeroShoppingActivityService zeroShoppingActivityService;

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView zeroShopping(){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/zero-shopping", "responsive", true);
        return modelAndView;
    }


}
