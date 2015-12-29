package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/robots.txt")
public class RobotsController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView robots() {
        return new ModelAndView("/robots");
    }
}
