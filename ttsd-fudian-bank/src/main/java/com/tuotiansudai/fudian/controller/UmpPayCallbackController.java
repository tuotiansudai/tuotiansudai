package com.tuotiansudai.fudian.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.service.UmpBindCardService;
import com.tuotiansudai.fudian.service.UmpRechargeService;
import com.tuotiansudai.fudian.service.UmpWithdrawService;
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
public class UmpPayCallbackController {

    static Logger logger = Logger.getLogger(UmpPayCallbackController.class);

    private final UmpRechargeService umpRechargeService;

    private final UmpWithdrawService umpWithdrawService;

    private final UmpBindCardService umpBindCardService;


    @Autowired
    public UmpPayCallbackController(UmpRechargeService umpRechargeService, UmpWithdrawService umpWithdrawService, UmpBindCardService umpBindCardService){
        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
        this.umpBindCardService = umpBindCardService;
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

//    @RequestMapping(value = "/mer_replace_card_notify", method = RequestMethod.GET)
//    public ModelAndView replaceBankCard(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        String responseData;
//        if ("mer_bind_card_apply_notify".equalsIgnoreCase(paramsMap.get("service"))) {
//            responseData = this.bindBankCardService.bindBankCardApplyCallback(paramsMap, request.getQueryString());
//        } else {
//            responseData = bindBankCardService.replaceBankCardCallback(paramsMap, request.getQueryString());
//        }
//        return new ModelAndView("/callback_response", "content", responseData);
//    }

//    @RequestMapping(value = "/withdraw_notify", method = RequestMethod.GET)
//    public ModelAndView withdrawNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        String responseData = this.withdrawService.withdrawCallback(paramsMap, request.getQueryString());
//        return new ModelAndView("/callback_response", "content", responseData);
//    }

//    @RequestMapping(value = "/normal_repay_notify", method = RequestMethod.GET)
//    public ModelAndView repayNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        try {
//            String responseData = this.normalRepayService.repayCallback(paramsMap, request.getQueryString());
//            return new ModelAndView("/callback_response", "content", responseData);
//        } catch (Exception e) {
//            logger.error(MessageFormat.format("[Normal Repay] repay callback is failed (queryString = {0})", request.getQueryString()), e);
//        }
//        return new ModelAndView("/callback_response", "content", "");
//    }

//    @RequestMapping(value = "/normal_repay_payback_notify", method = RequestMethod.GET)
//    public ModelAndView normalRepayPaybackNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        try {
//            String responseData = this.normalRepayService.investPaybackCallback(paramsMap, request.getQueryString());
//            return new ModelAndView("/callback_response", "content", responseData);
//        } catch (Exception e) {
//            logger.error(MessageFormat.format("[Normal Repay] repay payback callback is failed (queryString = {0})", request.getQueryString()), e);
//        }
//        return new ModelAndView("/callback_response", "content", "");
//    }

//    @RequestMapping(value = "/normal_repay_invest_fee_notify", method = RequestMethod.GET)
//    public ModelAndView repayInvestFeeNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        String responseData = this.normalRepayService.investFeeCallback(paramsMap, request.getQueryString());
//        return new ModelAndView("/callback_response", "content", responseData);
//    }

//    @RequestMapping(value = "/advance_repay_notify", method = RequestMethod.GET)
//    public ModelAndView advanceRepayNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        try {
//            String responseData = this.advanceRepayService.repayCallback(paramsMap, request.getQueryString());
//            return new ModelAndView("/callback_response", "content", responseData);
//        } catch (Exception e) {
//            logger.error(MessageFormat.format("[Advance Repay] repay callback is failed (queryString = {0})", request.getQueryString()), e);
//        }
//        return new ModelAndView("/callback_response", "content", "");
//    }

//    @RequestMapping(value = "/advance_repay_payback_notify", method = RequestMethod.GET)
//    public ModelAndView advanceRepayPaybackNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        try {
//            String responseData = this.advanceRepayService.investPaybackCallback(paramsMap, request.getQueryString());
//            return new ModelAndView("/callback_response", "content", responseData);
//        } catch (Exception e) {
//            logger.error(MessageFormat.format("[Advance Repay] repay payback callback is failed (queryString = {0})", request.getQueryString()), e);
//        }
//        return new ModelAndView("/callback_response", "content", "");
//    }

//    @RequestMapping(value = "/advance_repay_invest_fee_notify", method = RequestMethod.GET)
//    public ModelAndView advanceRepayInvestFeeNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        String responseData = this.advanceRepayService.investFeeCallback(paramsMap, request.getQueryString());
//        return new ModelAndView("/callback_response", "content", responseData);
//    }

//    @RequestMapping(value = "/coupon_repay_notify", method = RequestMethod.GET)
//    public ModelAndView couponRepayNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        try {
//            String responseData = this.couponRepayService.couponRepayCallback(paramsMap, request.getQueryString());
//            return new ModelAndView("/callback_response", "content", responseData);
//        } catch (Exception e) {
//            logger.error(MessageFormat.format("[Coupon Repay] coupon repay callback is failed (queryString = {0})", request.getQueryString()), e);
//        }
//        return new ModelAndView("/callback_response", "content", "");
//    }

//    @RequestMapping(value = "/extra_rate_notify", method = RequestMethod.GET)
//    public ModelAndView extraRateInvestNotify(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        String responseData = this.extraRateService.extraRateInvestCallback(paramsMap, request.getQueryString());
//        return new ModelAndView("/callback_response", "content", responseData);
//    }

//    @ResponseBody
//    @RequestMapping(value = "/experience_repay_notify", method = RequestMethod.GET)
//    public ModelAndView repayCallback(HttpServletRequest request) {
//        Map<String, String> paramsMap = this.parseRequestParameters(request);
//        String responseData = experienceRepayService.repayCallback(paramsMap, request.getQueryString());
//        return new ModelAndView("/callback_response", "content", responseData);
//    }

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
