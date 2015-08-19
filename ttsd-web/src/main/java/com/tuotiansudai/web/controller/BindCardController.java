package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/bind-card")
public class BindCardController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView bindCard() {
        return new ModelAndView("/bind-card");
    }

}
