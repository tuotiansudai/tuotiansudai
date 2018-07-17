package com.tuotiansudai.fudian.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.umpservice.*;
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
@RequestMapping(value = "/ump/callback")
public class UmpCallbackController {

    static Logger logger = Logger.getLogger(UmpCallbackController.class);

    private final UmpRechargeService umpRechargeService;

    private final UmpWithdrawService umpWithdrawService;

    private final UmpBindCardService umpBindCardService;

    private final UmpLoanRepayService umpLoanRepayService;

    private final UmpReplaceBindCardService umpReplaceBindCardService;

    private final UmpInvestRepayService umpInvestRepayService;

    private final UmpCouponRepayService umpCouponRepayService;

    private final UmpExtraRateRepayService umpExtraRateRepayService;

    private final UmpExperienceRepayService umpExperienceRepayService;

    private final UmpInvestRepayFeeService umpInvestRepayFeeService;


    @Autowired
    public UmpCallbackController(UmpRechargeService umpRechargeService, UmpWithdrawService umpWithdrawService,
                                 UmpBindCardService umpBindCardService, UmpLoanRepayService umpLoanRepayService,
                                 UmpReplaceBindCardService umpReplaceBindCardService, UmpInvestRepayService umpInvestRepayService,
                                 UmpCouponRepayService umpCouponRepayService, UmpExtraRateRepayService umpExtraRateRepayService,
                                 UmpExperienceRepayService umpExperienceRepayService, UmpInvestRepayFeeService umpInvestRepayFeeService){
        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
        this.umpBindCardService = umpBindCardService;
        this.umpLoanRepayService = umpLoanRepayService;
        this.umpReplaceBindCardService = umpReplaceBindCardService;
        this.umpInvestRepayService = umpInvestRepayService;
        this.umpCouponRepayService = umpCouponRepayService;
        this.umpExtraRateRepayService = umpExtraRateRepayService;
        this.umpExperienceRepayService = umpExperienceRepayService;
        this.umpInvestRepayFeeService = umpInvestRepayFeeService;
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
        if (paramsMap.containsKey("service")){
            responseData = umpWithdrawService.applyNotifyCallBack(paramsMap, request.getQueryString());
        }else {
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

    @RequestMapping(value = "/mer_replace_card_notify", method = RequestMethod.GET)
    public ModelAndView replaceBankCard(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData;
        if ("mer_bind_card_apply_notify".equalsIgnoreCase(paramsMap.get("service"))) {
            responseData = umpReplaceBindCardService.notifyCallBack(paramsMap, request.getQueryString());
        } else {
            responseData = umpReplaceBindCardService.applyNotifyCallBack(paramsMap, request.getQueryString());
        }
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/normal_repay_notify", method = RequestMethod.GET)
    public ModelAndView repayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpLoanRepayService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/advance_repay_notify", method = RequestMethod.GET)
    public ModelAndView advanceRepayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpLoanRepayService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/normal_repay_payback_notify", method = RequestMethod.GET)
    public ModelAndView normalRepayPaybackNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpInvestRepayService.normalNotifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/advance_repay_payback_notify", method = RequestMethod.GET)
    public ModelAndView advanceRepayPaybackNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpInvestRepayService.advanceNotifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/normal_repay_invest_fee_notify", method = RequestMethod.GET)
    public ModelAndView repayInvestFeeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpInvestRepayFeeService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/advance_repay_invest_fee_notify", method = RequestMethod.GET)
    public ModelAndView advanceRepayInvestFeeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpInvestRepayFeeService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/coupon_repay_notify", method = RequestMethod.GET)
    public ModelAndView couponRepayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpCouponRepayService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/extra_rate_notify", method = RequestMethod.GET)
    public ModelAndView extraRateInvestNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpExtraRateRepayService.notifyCallBack(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/experience_repay_notify", method = RequestMethod.GET)
    public ModelAndView repayCallback(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = umpExperienceRepayService.notifyCallBack(paramsMap, request.getQueryString());
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
