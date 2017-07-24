package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleHouseDecorateService;
import com.tuotiansudai.console.activity.service.ActivityConsoleMothersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class HouseDecorateController {

    @Autowired
    private ActivityConsoleHouseDecorateService activityConsoleHouseDecorateService;

    @RequestMapping(value = "/house-decorate-list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "index", defaultValue = "1") int index) {
        ModelAndView modelAndView = new ModelAndView("/house-decorate-list");
        final int pageSize = 10;
        modelAndView.addObject("data", activityConsoleHouseDecorateService.list(index, pageSize));
        return modelAndView;
    }
}
