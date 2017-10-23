package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import com.tuotiansudai.console.activity.service.ActivityConsoleZeroShoppingService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/zero-shopping")
public class ZeroShoppingController {

    @Autowired
    private ActivityConsoleZeroShoppingService activityConsoleZeroShoppingService;

    @RequestMapping(value = "user-prize-list", method = RequestMethod.GET)
    public ModelAndView userPrizeList(@RequestParam(name = "mobile", required = false) String mobile,
                                        @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                        @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        ModelAndView modelAndView = new ModelAndView("/zero-shopping-select-list");
        modelAndView.addObject("data", activityConsoleZeroShoppingService.userPrizeList(index, pageSize, mobile,
                startTime == null ? null : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? null : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).toDate()));
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }

    @RequestMapping(value = "config-prize-list", method = RequestMethod.GET)
    public ModelAndView configPrizeList() {
        ModelAndView modelAndView = new ModelAndView("/zero-shopping-prize-config");
        modelAndView.addObject("data", activityConsoleZeroShoppingService.getAllPrize());
        modelAndView.addObject("prizeTypes", ZeroShoppingPrize.getTaskZeroShoppingPrize());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "update-prize-count", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public ZeroShoppingPrizeConfigModel updatePrizeCount(@RequestBody ZeroShoppingPrizeConfigModel zeroShoppingPrizeConfigModel) {
        activityConsoleZeroShoppingService.updatePrizeCount(zeroShoppingPrizeConfigModel);
        return zeroShoppingPrizeConfigModel;
    }
}
