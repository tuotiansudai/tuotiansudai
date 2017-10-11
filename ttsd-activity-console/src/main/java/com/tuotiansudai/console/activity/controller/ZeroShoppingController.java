package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import com.tuotiansudai.console.activity.service.ActivityConsoleZeroShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class ZeroShoppingController {

    @Autowired
    private ActivityConsoleZeroShoppingService activityConsoleZeroShoppingService;

    @RequestMapping(value = "zero-shopping/user-prize-list", method = RequestMethod.GET)
    public ModelAndView selectPrizeList(@RequestParam(name = "mobile", required = false) String mobile,
                                        @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                        @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){

        ModelAndView modelAndView = new ModelAndView("/zero-shopping-select-list");
        modelAndView.addObject("data", activityConsoleZeroShoppingService.userPrizeList(index, pageSize, mobile, startTime, endTime));
        modelAndView.addObject("prizeTypes", ZeroShoppingPrize.getTaskZeroShoppingPrize());
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }

    @RequestMapping(value = "zero-shopping/config-prize-list", method = RequestMethod.GET)
    public ModelAndView selectPrizeList(){
        ModelAndView modelAndView = new ModelAndView("/zero-shopping-prize-config");
        modelAndView.addObject("data", activityConsoleZeroShoppingService.getAllPrize());
        modelAndView.addObject("prizeTypes", ZeroShoppingPrize.getTaskZeroShoppingPrize());
        return modelAndView;
    }

    @RequestMapping(value = "zero-shopping/update-prize-count", method = RequestMethod.POST)
    public ModelAndView updatePrizeCount(@RequestBody ZeroShoppingPrizeConfigModel zeroShoppingPrizeConfigModel){
        activityConsoleZeroShoppingService.updatePrizeCount(zeroShoppingPrizeConfigModel);
        return new ModelAndView("redirect:config-prize-list");
    }
}
