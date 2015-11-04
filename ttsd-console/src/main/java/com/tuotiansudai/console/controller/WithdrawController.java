package com.tuotiansudai.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class WithdrawController {

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public ModelAndView withdraw() {
        return new ModelAndView("/withdraw");
    }

}
