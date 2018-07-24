package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.console.service.BankDataQueryService;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.message.BankQueryLoanMessage;
import com.tuotiansudai.fudian.message.BankQueryTradeMessage;
import com.tuotiansudai.fudian.message.BankQueryUserMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(path = "/finance-manage/real-time-status")
public class BankQueryDataController {

    private final static Logger logger = Logger.getLogger(BankQueryDataController.class);

    private final BankDataQueryService bankDataQueryService;

    @Autowired
    public BankQueryDataController(BankDataQueryService bankDataQueryService) {
        this.bankDataQueryService = bankDataQueryService;
    }

    @RequestMapping
    public ModelAndView queryDefault() {
        return new ModelAndView("/real-time-status");
    }

    @RequestMapping(path = "/user")
    public ModelAndView queryUserStatus(@RequestParam(value = "loginNameOrMobile") String loginNameOrMobile) {
        BankQueryUserMessage bankQueryUserMessage = bankDataQueryService.getUserStatus(loginNameOrMobile);

        return new ModelAndView("/real-time-status", "type", "user")
                .addObject("loginNameOrMobile", loginNameOrMobile)
                .addObject("data", bankQueryUserMessage);
    }

    @RequestMapping(path = "/loan")
    public ModelAndView queryLoanStatus(@RequestParam(value = "loanId") long loanId) {
        BankQueryLoanMessage bankQueryLoanMessage = bankDataQueryService.getLoanStatus(loanId);

        return new ModelAndView("/real-time-status", "type", "loan")
                .addObject("loanId", loanId)
                .addObject("data", bankQueryLoanMessage);
    }

    @RequestMapping(path = "/trade")
    public ModelAndView queryTrade(@RequestParam(value = "orderNo") String orderNo,
                                   @RequestParam(value = "orderDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
                                   @RequestParam(value = "queryTradeType") QueryTradeType queryTradeType) {
        BankQueryTradeMessage bankQueryTradeMessage = bankDataQueryService.getTradeStatus(orderNo, orderDate, queryTradeType);

        return new ModelAndView("/real-time-status", "type", "trade")
                .addObject("orderNo", orderNo)
                .addObject("orderDate", orderDate)
                .addObject("queryTradeType", queryTradeType)
                .addObject("data", bankQueryTradeMessage);
    }

    @RequestMapping(value = "/account-bill", method = RequestMethod.GET)
    public ModelAndView queryAccountBill(@RequestParam(value = "loginNameOrMobile", required = false) String loginNameOrMobile,
                                         @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        if (Strings.isNullOrEmpty(loginNameOrMobile) || startDate == null || endDate == null) {
            return new ModelAndView("/bank-account-bill");
        }
        ModelAndView modelAndView = new ModelAndView("/bank-account-bill", "data", bankDataQueryService.getAccountBill(loginNameOrMobile, startDate, endDate));
        modelAndView.addObject("loginNameOrMobile", loginNameOrMobile);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        return modelAndView;
    }

    @RequestMapping(value = "/loan-bill", method = RequestMethod.GET)
    public ModelAndView queryLoanBill(@RequestParam(value = "loanId", required = false) Long loanId) {
        if (loanId == null) {
            return new ModelAndView("/bank-loan-bill");
        }

        ModelAndView modelAndView = new ModelAndView("/bank-loan-bill", "data", bankDataQueryService.getLoanBill(loanId));
        modelAndView.addObject("loanId", loanId);
        return modelAndView;
    }
}
