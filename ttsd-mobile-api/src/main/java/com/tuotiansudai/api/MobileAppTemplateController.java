package com.tuotiansudai.api;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/api-template")
public class MobileAppTemplateController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView mediaCenter() {
        return new ModelAndView("/api-template");
    }
}
