package com.tuotiansudai.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/error")
public class ErrorController {

    @RequestMapping(path = "/{code:^404|500$}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView error(@PathVariable String code) {
        return new ModelAndView("/error/" + code);
    }
}
