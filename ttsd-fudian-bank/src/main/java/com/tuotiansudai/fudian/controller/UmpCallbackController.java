package com.tuotiansudai.fudian.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.umpservice.UmpBindCardService;
import com.tuotiansudai.fudian.umpservice.UmpLoanRepayService;
import com.tuotiansudai.fudian.umpservice.UmpRechargeService;
import com.tuotiansudai.fudian.umpservice.UmpWithdrawService;
import com.tuotiansudai.fudian.util.UmpUtils;
import com.umpay.api.exception.VerifyException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;


@Controller
@RequestMapping(value = "/ump/notify-url/callback")
public class UmpCallbackController {

    static Logger logger = Logger.getLogger(UmpCallbackController.class);

    private final UmpRechargeService umpRechargeService;

    private final UmpWithdrawService umpWithdrawService;

    private final UmpBindCardService umpBindCardService;

    private final UmpLoanRepayService umpLoanRepayService;

    private final UmpUtils umpUtils;

    @Autowired
    public UmpCallbackController(UmpRechargeService umpRechargeService,
                                 UmpWithdrawService umpWithdrawService,
                                 UmpBindCardService umpBindCardService,
                                 UmpLoanRepayService umpLoanRepayService,
                                 UmpUtils umpUtils) {
        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
        this.umpBindCardService = umpBindCardService;
        this.umpLoanRepayService = umpLoanRepayService;
        this.umpUtils = umpUtils;

    }

    @RequestMapping(value = "/recharge_notify", method = RequestMethod.GET)
    public ModelAndView rechargeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.umpRechargeService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/withdraw_notify", method = RequestMethod.GET)
    public ModelAndView withdrawNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData;
        if (paramsMap.containsKey("service")) {
            responseData = umpWithdrawService.applyNotifyCallBack(paramsMap, request.getQueryString());
        } else {
            responseData = umpWithdrawService.notifyCallBack(paramsMap, request.getQueryString());
        }
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/mer_bind_card_notify", method = RequestMethod.GET)
    public ModelAndView bindBankCard(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData;
        if ("mer_bind_card_apply_notify".equalsIgnoreCase(paramsMap.get("service"))) {
            responseData = umpBindCardService.applyNotifyCallBack(paramsMap, request.getQueryString());
        } else {
            responseData = umpBindCardService.notifyCallBack(paramsMap, request.getQueryString());
        }
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/loan_repay_notify", method = RequestMethod.GET)
    public ModelAndView repayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpLoanRepayService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/repay_payback_notify", method = RequestMethod.GET)
    public ModelAndView normalRepayPaybackNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpLoanRepayService.repayPaybackNotifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/repay_fee_notify", method = RequestMethod.GET)
    public ModelAndView repayInvestFeeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpLoanRepayService.repayFeeNotifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/coupon_repay_notify", method = RequestMethod.GET)
    public ModelAndView couponRepayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpLoanRepayService.couponRepayNotifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/extra_rate_notify", method = RequestMethod.GET)
    public ModelAndView extraRateInvestNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpLoanRepayService.extraRepayNotifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/validate-front-callback", method = RequestMethod.POST)
    public ResponseEntity validate(@RequestBody Map<String, String> params) {
        try {
            if (umpUtils.validateCallBack(params)){
                return ResponseEntity.ok().build();
            }
        } catch (VerifyException ignored) {
        }
        return ResponseEntity.badRequest().build();
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
