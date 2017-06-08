package com.tuotiansudai.console.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class CelebrationSingleController {

   // @Autowired
  //  private ActivityConsoleSingleService activityConsoleSingleService;

    @RequestMapping(value = "/single-list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "index", defaultValue = "1") int index) {
        ModelAndView modelAndView = new ModelAndView("/celebration-single-list");
        final int pageSize = 10;
      //  modelAndView.addObject("data", activityConsoleSingleService.investList(index, pageSize));
        return modelAndView;
    }
}
