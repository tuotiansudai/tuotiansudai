package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.model.CashSnowballActivityView;
import com.tuotiansudai.console.activity.service.ActivityConsoleCashSnowballService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class CashSnowballController {

    @Autowired
    private ActivityConsoleCashSnowballService activityConsoleCashSnowballService;

    @RequestMapping(value = "cash-snowball-list")
    public ModelAndView cashSnowballList(@RequestParam(value = "index", defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(name = "mobile", required = false) String mobile,
                                         @RequestParam(name = "startInvestAmount", required = false) Long startInvestAmount,
                                         @RequestParam(name = "endInvestAmount", required = false) Long endInvestAmount) {
        ModelAndView modelAndView = new ModelAndView("/cash-snowball-list");
        BasePaginationDataDto<CashSnowballActivityView> basePaginationDataDto = activityConsoleCashSnowballService.list(index, pageSize, mobile, startInvestAmount, endInvestAmount);
        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("sumInvestAmount", basePaginationDataDto.getRecords().stream().mapToLong(i -> i.getInvestAmount()).sum());
        modelAndView.addObject("sumAnnualizedAmount", basePaginationDataDto.getRecords().stream().mapToLong(i -> i.getAnnualizedAmount()).sum());
        modelAndView.addObject("sumCashAmount", basePaginationDataDto.getRecords().stream().mapToLong(i -> i.getCashAmount()).sum());
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startInvestAmount", startInvestAmount == null ? null : String.valueOf(startInvestAmount));
        modelAndView.addObject("endInvestAmount", endInvestAmount == null ? null : String.valueOf(endInvestAmount));
        return modelAndView;
    }
}
