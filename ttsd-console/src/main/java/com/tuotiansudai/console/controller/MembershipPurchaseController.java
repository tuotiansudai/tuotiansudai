package com.tuotiansudai.console.controller;

import com.tuotiansudai.membership.service.MembershipPurchaseService;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.CalculateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(path = "/membership-manage")
public class MembershipPurchaseController {

    @Autowired
    private MembershipPurchaseService membershipPurchaseService;

    @RequestMapping(path = "/membership-purchase")
    public ModelAndView getPurchaseList(@RequestParam(value = "mobile", required = false) String mobile,
                                        @RequestParam(value = "duration", required = false) Integer duration,
                                        @RequestParam(value = "source", required = false) Source source,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "index", defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/membership-purchase", "data", membershipPurchaseService.getMembershipPurchaseList(mobile, duration, source,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                index, pageSize));

        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("duration", duration);
        modelAndView.addObject("source", source);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);

        return modelAndView;
    }
}
