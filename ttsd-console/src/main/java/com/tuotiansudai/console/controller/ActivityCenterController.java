package com.tuotiansudai.console.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/activity-manage")
public class ActivityCenterController {

    static Logger logger = Logger.getLogger(ActivityCenterController.class);

    @RequestMapping(value = "/activity-center", method = RequestMethod.GET)
    public ModelAndView activityCenter(){
        return new ModelAndView("/activity-center-edit");
    }

}
