package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.console.service.ConsoleUMPayRealTimeStatusService;
import com.tuotiansudai.fudian.message.BankQueryLoanMessage;
import com.tuotiansudai.fudian.message.BankQueryUserMessage;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.UMPayRealTimeStatusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(path = "/finance-manage/real-time-status")
public class UMPayRealTimeStatusController {

    private final static Logger logger = Logger.getLogger(UMPayRealTimeStatusController.class);

    @Autowired
    private ConsoleUMPayRealTimeStatusService consoleUMPayRealTimeStatusService;

    @Autowired
    private UMPayRealTimeStatusService umPayRealTimeStatusService;

    @RequestMapping(path = "/user")
    public ModelAndView queryUserStatus(@RequestParam(value = "loginNameOrMobile") String loginNameOrMobile) {
        BankQueryUserMessage bankQueryUserMessage = consoleUMPayRealTimeStatusService.getUserStatus(loginNameOrMobile);

        return new ModelAndView("/real-time-status", "type", "user")
                .addObject("loginNameOrMobile", loginNameOrMobile)
                .addObject("data", bankQueryUserMessage);
    }

    @RequestMapping(path = "/loan")
    public ModelAndView queryLoanStatus(@RequestParam(value = "loanId") long loanId) {
        BankQueryLoanMessage bankQueryLoanMessage = consoleUMPayRealTimeStatusService.getLoanStatus(loanId);

        return new ModelAndView("/real-time-status", "type", "loan")
                .addObject("loanId", loanId)
                .addObject("data", bankQueryLoanMessage);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getStatus(@RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "loginNameOrMobile", required = false) String loginNameOrMobile,
                                  @RequestParam(value = "loanId", required = false) String loanId,
                                  @RequestParam(value = "orderId", required = false) String orderId,
                                  @RequestParam(value = "merDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date merDate,
                                  @RequestParam(value = "businessType", required = false) String businessType) {
        Map<String, String> data = null;
        if (!Strings.isNullOrEmpty(type)) {
            switch (type) {
                case "platform":
                    data = umPayRealTimeStatusService.getPlatformStatus();
                    break;
                case "transfer":
                    if (orderId != null && merDate != null && businessType != null) {
                        data = consoleUMPayRealTimeStatusService.getTransferStatus(orderId, merDate, businessType);
                    }
                    break;
            }
        }

        ModelAndView modelAndView = new ModelAndView("/real-time-status");
        modelAndView.addObject("data", data);
        modelAndView.addObject("type", type);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("orderId", orderId);
        modelAndView.addObject("merDate", merDate);
        modelAndView.addObject("businessType", businessType);
        return modelAndView;
    }
}
