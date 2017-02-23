package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.service.ActivityWomanDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class WomanDayAcitivtyController {
    @Autowired
    ActivityWomanDayService activityWomandayService;

    @RequestMapping(value = "/women-day-list", method = RequestMethod.GET)
    public ModelAndView getNotWorkList(@RequestParam(value = "index",defaultValue = "1") int index) {
        ModelAndView modelAndView = new ModelAndView("/women-day-list");
        final int pageSize = 10;
        modelAndView.addObject("data", activityWomandayService.getWomanDayPrizeRecord(index, pageSize, null));
        return modelAndView;
    }
}
