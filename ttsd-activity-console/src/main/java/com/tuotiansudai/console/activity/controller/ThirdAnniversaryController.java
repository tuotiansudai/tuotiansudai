package com.tuotiansudai.console.activity.controller;


import com.tuotiansudai.console.activity.service.ActivityConsoleThirdAnniversaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/third-anniversary-list")
public class ThirdAnniversaryController {

    @Autowired
    private ActivityConsoleThirdAnniversaryService activityConsoleThirdAnniversaryService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "index", defaultValue = "1") int index,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/third-anniversary-list");
        modelAndView.addObject("data", activityConsoleThirdAnniversaryService.list(index, pageSize));
        return modelAndView;
    }
}
