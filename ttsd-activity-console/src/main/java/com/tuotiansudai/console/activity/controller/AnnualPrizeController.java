package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleAnnualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class AnnualPrizeController {
    @Autowired
    private ActivityConsoleAnnualService activityConsoleAnnualService;

    @RequestMapping(value = "/annual-list", method = RequestMethod.GET)
    public ModelAndView getAnnualList(@RequestParam(value = "index", defaultValue = "1") int index,
                                      @RequestParam(value = "mobile", required = false) String mobile) {

        ModelAndView modelAndView = new ModelAndView("/annual-prize-list");
        final int pageSize = 10;
        modelAndView.addObject("data", activityConsoleAnnualService.findAnnualList(index, pageSize, mobile));
        return modelAndView;
    }
}
