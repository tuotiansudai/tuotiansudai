package com.tuotiansudai.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/error")
public class ErrorPageController {
    @RequestMapping(path = "/{item}", method = RequestMethod.GET)
    public ModelAndView account(@PathVariable String item){
        return new ModelAndView("/error/" + item);
    }
}
