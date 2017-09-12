package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleNationalMidAutumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class NationalMidAutumnController {

    @Autowired
    private ActivityConsoleNationalMidAutumnService activityConsoleNationalMidAutumnService;

    @RequestMapping(value = "/national-mid-autumn-list")
    public ModelAndView nationalMidAutumnList(@RequestParam(value = "index", defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("national-mid-autumn-list");
        modelAndView.addObject("data", activityConsoleNationalMidAutumnService.list(index, pageSize));
        return modelAndView;
    }
}
