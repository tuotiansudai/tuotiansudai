package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.tuotiansudai.console.service.BankDataQueryService;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.message.BankQueryLoanMessage;
import com.tuotiansudai.fudian.message.BankQueryTradeMessage;
import com.tuotiansudai.fudian.message.BankQueryUserMessage;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public ModelAndView queryUserStatus(@RequestParam(value = "role",defaultValue = "INVESTOR") Role role, @RequestParam(value = "loginNameOrMobile") String loginNameOrMobile) {
        BankQueryUserMessage bankQueryUserMessage = bankDataQueryService.getUserStatus(role, loginNameOrMobile);

        return new ModelAndView("/real-time-status", "type", "user")
                .addObject("loginNameOrMobile", loginNameOrMobile)
                .addObject("data", bankQueryUserMessage)
                .addObject("role", role);
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
    public ModelAndView queryAccountBill(@RequestParam(value = "role",defaultValue = "INVESTOR") Role role,
                                         @RequestParam(value = "loginNameOrMobile", required = false) String loginNameOrMobile,
                                         @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        if (Strings.isNullOrEmpty(loginNameOrMobile) || startDate == null || endDate == null) {
            return new ModelAndView("/bank-account-bill");
        }
        ModelAndView modelAndView = new ModelAndView("/bank-account-bill", "data", bankDataQueryService.getAccountBill(role, loginNameOrMobile, startDate, endDate));
        modelAndView.addObject("loginNameOrMobile", loginNameOrMobile);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("role", role);
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

    @RequestMapping(value = "/ump", method = RequestMethod.GET)
    public ModelAndView getStatus(@RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "mobile", required = false) String mobile,
                                  @RequestParam(value = "loanId", required = false) String loanId,
                                  @RequestParam(value = "orderId", required = false) String orderId,
                                  @RequestParam(value = "merDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date merDate,
                                  @RequestParam(value = "businessType", required = false) String businessType) {
        Map<String, String> data = bankDataQueryService.getUmpData(type, mobile, loanId, orderId, merDate, businessType);
        ModelAndView modelAndView = new ModelAndView("/ump-real-time-status");
        modelAndView.addObject("data", data);
        modelAndView.addObject("type", type);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("orderId", orderId);
        modelAndView.addObject("merDate", merDate);
        modelAndView.addObject("businessType", businessType);
        return modelAndView;
    }

    @RequestMapping(value = "/ump/transfer-bill", method = RequestMethod.GET)
    public ModelAndView transferBill(@RequestParam(value = "loginName", required = false) String loginName,
                                     @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                     @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        ModelAndView modelAndView = new ModelAndView("/transfer-bill", "data", Lists.newArrayList());
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        if (StringUtils.isEmpty(loginName) || startDate == null || endDate == null) {
            return modelAndView;
        }
        List<List<String>> data = new ArrayList<>();
        DateTime queryStartDate = new DateTime(startDate).withTimeAtStartOfDay();
        while (queryStartDate.isBefore(new DateTime(endDate).withTimeAtStartOfDay()) || queryStartDate.isEqual(new DateTime(endDate).withTimeAtStartOfDay())) {
            DateTime queryEndDate = new DateTime(endDate).withTimeAtStartOfDay();
            if (Days.daysBetween(queryStartDate, new DateTime(endDate).withTimeAtStartOfDay()).getDays() >= 30) {
                queryEndDate = queryStartDate.plusDays(29);
            }
            List<List<String>> transferBill = bankDataQueryService.getUmpTransferBill(loginName, queryStartDate.toDate(), queryEndDate.toDate());
            if (!CollectionUtils.isEmpty(transferBill)) {
                data.addAll(transferBill);
            }
            queryStartDate = queryEndDate.plusDays(1);
        }
        Collections.sort(data, (t1, t2) -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return Longs.compare(simpleDateFormat.parse(t1.get(1)).getTime(), simpleDateFormat.parse(t2.get(1)).getTime());
            } catch (ParseException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            return 0;
        });
        modelAndView.addObject("data", data);
        return modelAndView;
    }

}
