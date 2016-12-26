package com.tuotiansudai.console.activity.controller;


import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.console.activity.service.ActivityConsoleUserLotteryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class HeadlinesTodayController {

    static Logger logger = Logger.getLogger(HeadlinesTodayController.class);

    @Autowired
    private ActivityConsoleUserLotteryService activityConsoleUserLotteryService;

    @RequestMapping(value = "/headlines-today-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView userPrizeList(@RequestParam(name = "mobile", required = false) String mobile,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "authenticationType", defaultValue = "", required = false) String authenticationType) {
        ModelAndView modelAndView = new ModelAndView("/headlines-today-list");
        modelAndView.addObject("headlinesTodayList", activityConsoleUserLotteryService.findUserLotteryPrizeViewsByHeadlinesToday(mobile, null, ActivityCategory.HEADLINES_TODAY_ACTIVITY, startTime, endTime, null, null, authenticationType));
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("authenticationType", authenticationType);
        return modelAndView;
    }

}
