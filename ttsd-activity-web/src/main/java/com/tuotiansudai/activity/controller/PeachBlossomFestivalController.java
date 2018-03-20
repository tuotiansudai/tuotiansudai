package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/activity/peach-bloassom")
public class PeachBlossomFestivalController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView national() {
        return new ModelAndView("/activities/2018/peach-blossom-festival");
    }
}
