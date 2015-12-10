package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/my-treasure")
public class MyTreasureController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(){
        return new ModelAndView("/my-treasure");
    }
}
