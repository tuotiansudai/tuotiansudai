package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleStartWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class StartWorkController {

    @Autowired
    private ActivityConsoleStartWorkService activityConsoleStartWorkService;

    @RequestMapping(value = "/start-work-list", method = RequestMethod.GET)
    public ModelAndView startWorkList(@RequestParam(value = "mobile", required = false) String mobile,
                                      @RequestParam(value = "userName", required = false) String userName,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index", defaultValue = "1") int index,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        ModelAndView modelAndView = new ModelAndView("/start-work-list");
        modelAndView.addObject("data", activityConsoleStartWorkService.list(mobile, userName, startTime, endTime, index, pageSize));
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }
}
