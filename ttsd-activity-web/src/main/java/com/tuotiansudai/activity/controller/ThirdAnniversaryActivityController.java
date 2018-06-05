package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/third-anniversary")
public class ThirdAnniversaryActivityController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView thirdAnniversary(){
        ModelAndView modelAndView = new ModelAndView("/activities/2018/third-anniversary", "responsive", true);

        return modelAndView;
    }

}
