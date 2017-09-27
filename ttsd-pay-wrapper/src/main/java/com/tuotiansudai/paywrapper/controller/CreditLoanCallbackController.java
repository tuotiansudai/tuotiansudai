package com.tuotiansudai.paywrapper.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.credit.CreditLoanActivateAccountService;
import com.tuotiansudai.paywrapper.credit.CreditLoanOutService;
import com.tuotiansudai.paywrapper.credit.CreditLoanRepayService;
import com.tuotiansudai.paywrapper.credit.CreditLoanRechargeService;
import com.tuotiansudai.paywrapper.credit.CreditLoanTransferAgentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;


@Controller
@RequestMapping(value = "/callback")
public class CreditLoanCallbackController {

    private static Logger logger = Logger.getLogger(CreditLoanCallbackController.class);

    private final CreditLoanOutService creditLoanOutService;

    private final CreditLoanActivateAccountService creditLoanActivateAccountService;

    private final CreditLoanRepayService creditLoanRepayService;

    private CreditLoanRechargeService creditLoanRechargeService;

    private CreditLoanTransferAgentService creditLoanTransferAgentService;

    @Autowired
    public CreditLoanCallbackController(CreditLoanOutService creditLoanOutService,
                                        CreditLoanActivateAccountService creditLoanActivateAccountService,
                                        CreditLoanRepayService creditLoanRepayService,
                                        CreditLoanRechargeService creditLoanRechargeService,
                                        CreditLoanTransferAgentService creditLoanTransferAgentService) {
        this.creditLoanOutService = creditLoanOutService;
        this.creditLoanActivateAccountService = creditLoanActivateAccountService;
        this.creditLoanRepayService = creditLoanRepayService;
        this.creditLoanRechargeService = creditLoanRechargeService;
        this.creditLoanTransferAgentService = creditLoanTransferAgentService;
    }

    @RequestMapping(value = "/credit_loan_out_notify", method = RequestMethod.GET)
    public ModelAndView loanOutNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.creditLoanOutService.loanOutCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/activate_account_notify", method = RequestMethod.GET)
    public ModelAndView ActivateAccountNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.creditLoanActivateAccountService.activateAccountCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/credit_loan_repay_notify", method = RequestMethod.GET)
    public ModelAndView repayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.creditLoanRepayService.repayCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/credit_loan_recharge_notify", method = RequestMethod.GET)
    public ModelAndView creditLoanRechargeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.creditLoanRechargeService.creditLoanRechargeCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/credit_loan_transfer_agent_notify", method = RequestMethod.GET)
    public ModelAndView creditLoanTransferAgentNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.creditLoanTransferAgentService.creditLoanTransferAgentCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    private Map<String, String> parseRequestParameters(HttpServletRequest request) {
        Map<String, String> paramsMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String parameter = request.getParameter(name);
            paramsMap.put(name, parameter);
        }
        return paramsMap;
    }
}
