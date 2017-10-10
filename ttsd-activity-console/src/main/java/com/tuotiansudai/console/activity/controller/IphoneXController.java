package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleIphoneXService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.IphoneXActivityDto;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class IphoneXController {

    @Autowired
    private ActivityConsoleIphoneXService activityConsoleIphoneXService;

    @RequestMapping(value = "iphonex-list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "index", defaultValue = "1") int index,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/iphonex-list");
        modelAndView.addObject("data", activityConsoleIphoneXService.list(index, pageSize));
        BasePaginationDataDto<IphoneXActivityDto> list = activityConsoleIphoneXService.list(1, Integer.MAX_VALUE);
        modelAndView.addObject("sumInvest", list.getRecords().stream().mapToLong(i->AmountConverter.convertStringToCent(i.getSumInvestAmount())).sum());
        modelAndView.addObject("sumCash", list.getRecords().stream().mapToLong(i->AmountConverter.convertStringToCent(i.getReward())).sum());
        modelAndView.addObject("sumAnnualized", list.getRecords().stream().mapToLong(i->AmountConverter.convertStringToCent(i.getSumAnnualizedAmount())).sum());
        return modelAndView;
    }
}
