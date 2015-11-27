package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.service.UMPayRealTimeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(path = "/finance-manage/real-time-status")
public class UMPayRealTimeStatusController {

    @Autowired
    private UMPayRealTimeStatusService payRealTimeStatusService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getStatus(@RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "loginName", required = false) String loginName,
                                  @RequestParam(value = "loanId", required = false) Long loanId) {
        Map<String, String> data = null;
        if (!Strings.isNullOrEmpty(type)) {
            switch (type) {
                case "user":
                    if (!Strings.isNullOrEmpty(loginName)) {
                        data = payRealTimeStatusService.getUserStatus(loginName);
                    }
                    break;
                case "platform":
                    data = payRealTimeStatusService.getPlatformStatus();
                    break;
                case "loan":
                    if (loanId != null) {
                        data = payRealTimeStatusService.getLoanStatus(loanId);
                    }
            }
        }

        ModelAndView modelAndView = new ModelAndView("/real-time-status");
        modelAndView.addObject("data", data);
        modelAndView.addObject("type", type);
        return modelAndView;
    }
}
