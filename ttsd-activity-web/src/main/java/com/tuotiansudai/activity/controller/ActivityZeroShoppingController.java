package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/zero-shopping")
public class ActivityZeroShoppingController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView zeroShopping() {
        return new ModelAndView("/activities/2017/purchas_zero", "responsive", true);
    }
}
