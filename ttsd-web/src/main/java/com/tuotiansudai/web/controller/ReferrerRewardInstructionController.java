package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/events/refer-reward-instruction")
public class ReferrerRewardInstructionController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView instruction() {
        return new ModelAndView("/events/refer-reward-instruction");
    }
}
