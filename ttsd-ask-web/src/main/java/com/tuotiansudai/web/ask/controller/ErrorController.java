package com.tuotiansudai.web.ask.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/error")
public class ErrorController {

    @Value("${web.server}")
    private String webServer;

    @RequestMapping(path = "/{code:^404|500$}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView error(@PathVariable String code) {
        return new ModelAndView(MessageFormat.format("redirect:{0}/error/{1}", webServer, code));
    }
}