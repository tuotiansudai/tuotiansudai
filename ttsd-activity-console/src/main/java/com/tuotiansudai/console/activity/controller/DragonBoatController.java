package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleDragonBoatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class DragonBoatController {

    @Autowired
    private ActivityConsoleDragonBoatService activityConsoleDragonBoatService;

    @RequestMapping(value = "/dragon-boat", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "index", defaultValue = "1") int index) {
        ModelAndView modelAndView = new ModelAndView("/dragon-boat");
        final int pageSize = 10;
        modelAndView.addObject("data", activityConsoleDragonBoatService.getList(index, pageSize));
        return modelAndView;
    }
}
