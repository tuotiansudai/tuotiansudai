package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity")
public class ActivitiesController {

    @RequestMapping(path = "/{item:^ranking|recruit$}", method = RequestMethod.GET)
    public ModelAndView activities(@PathVariable String item) {
        return new ModelAndView("/activities/" + item, "responsive", true);
    }
}
