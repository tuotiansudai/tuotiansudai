package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualized;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualizedView;
import com.tuotiansudai.console.activity.service.ActivityConsoleInvestAnnualizedService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class InvestAnnualizedController {

    @Autowired
    private ActivityConsoleInvestAnnualizedService activityConsoleInvestAnnualizedService;

    @RequestMapping(value = "/invest-annualized-list", method = RequestMethod.GET)
    public ModelAndView investAnnualizedList(@RequestParam(value = "activityInvestAnnualized", defaultValue = "NEW_YEAR_ACTIVITY") ActivityInvestAnnualized activityInvestAnnualized,
                                             @RequestParam(value = "mobile", required = false) String mobile,
                                             @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/invest-annualized-list");
        BasePaginationDataDto<ActivityInvestAnnualizedView> data = activityConsoleInvestAnnualizedService.list(activityInvestAnnualized, mobile, index, pageSize);
        modelAndView.addObject("data", data);
        modelAndView.addObject("sumInvestAmount", data.getRecords().stream().mapToLong(i->i.getSumInvestAmount()).sum());
        modelAndView.addObject("sumAnnualizedAmount", data.getRecords().stream().mapToLong(i->i.getSumAnnualizedAmount()).sum());
        modelAndView.addObject("activityInvestAnnualizeds", Arrays.asList(ActivityInvestAnnualized.values()));
        modelAndView.addObject("selectType", activityInvestAnnualized);
        modelAndView.addObject("mobile", mobile);
        return modelAndView;
    }
}
