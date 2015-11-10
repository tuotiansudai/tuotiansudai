package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoginLogPaginationItemDataDto;
import com.tuotiansudai.service.SecurityLogService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/security-log")
public class SecurityLogController {

    @Autowired
    private SecurityLogService securityLogService;

    @RequestMapping(path = "/login-log", method = RequestMethod.GET)
    public ModelAndView loginLog(@RequestParam(name = "loginName", required = false) String loginName,
                                 @RequestParam(name = "selectedYear", required = false) Integer selectedYear,
                                 @RequestParam(name = "selectedMonth", required = false) Integer selectedMonth,
                                 @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                 @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                 @RequestParam(name = "success", required = false) Boolean success) {

        DateTime now = new DateTime();
        if (selectedYear == null || selectedYear > now.getYear()) {
            selectedYear = now.getYear();
            selectedMonth = now.getMonthOfYear();
        }
        if (selectedMonth == null || selectedYear > now.getYear() || (selectedYear == now.getYear() && selectedMonth > now.getMonthOfYear())) {
            selectedMonth = now.getMonthOfYear();
        }


        BasePaginationDataDto<LoginLogPaginationItemDataDto> data = securityLogService.getLoginLogPaginationData(loginName, success, index, pageSize, selectedYear, selectedMonth);

        ModelAndView modelAndView = new ModelAndView("/login-log");

        modelAndView.addObject("data", data);
        modelAndView.addObject("years", Lists.newArrayList("2015", "2016", "2017", "2018", "2019", "2020"));
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("selectedYear", String.valueOf(selectedYear));
        modelAndView.addObject("selectedMonth", String.valueOf(selectedMonth));
        modelAndView.addObject("success", success);

        return modelAndView;
    }
}
