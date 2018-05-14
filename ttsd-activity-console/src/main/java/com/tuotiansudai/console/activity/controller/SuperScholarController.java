package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleSuperScholarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class SuperScholarController {

    @Autowired
    private ActivityConsoleSuperScholarService activityConsoleSuperScholarService;

    @RequestMapping(value = "/super-scholar-list")
    public ModelAndView list(@RequestParam(value = "keyWord", required = false) String keyWord,
                             @RequestParam(value = "index", defaultValue = "1") int index,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ModelAndView modelAndView = new ModelAndView("/super-scholar-list");
        modelAndView.addObject("data", activityConsoleSuperScholarService.list(keyWord, index, pageSize));
        modelAndView.addObject("keyWord", keyWord);
        return modelAndView;
    }
}
