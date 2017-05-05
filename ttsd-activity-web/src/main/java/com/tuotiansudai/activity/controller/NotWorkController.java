package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.NotWorkService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


public class NotWorkController {
    @Autowired
    private NotWorkService notWorkService;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.endTime}\")}")
    private Date activityEndTime;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHome() {
        ModelAndView modelAndView = new ModelAndView("/activities/no-work", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("expired", !(activityStartTime.before(new Date()) && activityEndTime.after(new Date())));
        if (!Strings.isNullOrEmpty(loginName)) {
            modelAndView.addObject("investAmount", AmountConverter.convertCentToString(notWorkService.getUsersActivityInvestAmount(loginName)));
            modelAndView.addObject("needInvestAmount", AmountConverter.convertCentToString(notWorkService.getUsersNeedInvestAmount(loginName)));
        }
        return modelAndView;
    }
}
