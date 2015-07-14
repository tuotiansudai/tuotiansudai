package com.ttsd.mobile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/certification")
public class CertificationController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView certification() {
        return new ModelAndView("/certification");
    }
}
