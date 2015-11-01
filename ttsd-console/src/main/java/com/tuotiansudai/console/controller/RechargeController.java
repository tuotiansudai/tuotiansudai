package com.tuotiansudai.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class RechargeController {

    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public ModelAndView recharge() {
        return new ModelAndView("/recharge");
    }

}
