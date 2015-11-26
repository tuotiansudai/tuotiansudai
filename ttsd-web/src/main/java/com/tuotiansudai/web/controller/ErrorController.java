package com.tuotiansudai.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/error")
public class ErrorController {

    @RequestMapping(path = "/{code}", method = RequestMethod.GET)
    public ModelAndView error(@PathVariable String code){
        return new ModelAndView("/error/" + code);
    }
}
