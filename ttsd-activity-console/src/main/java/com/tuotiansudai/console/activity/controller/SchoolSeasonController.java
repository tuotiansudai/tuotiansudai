package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleSchoolSeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/school-season")
public class SchoolSeasonController {

    @Autowired
    private ActivityConsoleSchoolSeasonService schoolSeasonService;

    @RequestMapping(value = "/school-season-list")
    public ModelAndView schoolSeasonList(@RequestParam(value = "index", defaultValue = "1") int index,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ModelAndView modelAndView=new ModelAndView("school-season-list");
        modelAndView.addObject("data",schoolSeasonService.list(index,pageSize));
        return modelAndView;
    }
}
