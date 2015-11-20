package com.tuotiansudai.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/help")
public class HelpCenterController {
    @RequestMapping(path = "/{item}", method = RequestMethod.GET)
    public ModelAndView account(@PathVariable String item){
        return new ModelAndView("/help/" + item);
    }
}
