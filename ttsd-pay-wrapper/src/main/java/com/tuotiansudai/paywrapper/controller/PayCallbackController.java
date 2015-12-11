package com.tuotiansudai.paywrapper.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.repository.model.UmPayService;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Map;


@Controller
@RequestMapping(value = "/callback")
public class PayCallbackController {

    static Logger logger = Logger.getLogger(PayCallbackController.class);

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private InvestService investService;

    @Resource(name = "normalRepayServiceImpl")
    private RepayService normalRepayService;

    @Resource(name = "advanceRepayServiceImpl")
    private RepayService advanceRepayService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/recharge_notify", method = RequestMethod.GET)
    public ModelAndView rechargeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.rechargeService.rechargeCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/mer_bind_agreement_notify", method = RequestMethod.GET)
    public ModelAndView agreementNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = agreementService.agreementCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/mer_bind_card_notify", method = RequestMethod.GET)
    public ModelAndView bindBankCard(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData;
        String service = paramsMap.get("service");
        if(UmPayService.NOTIFY_MER_BIND_CARD_APPLY.getServiceName().equals(service)){
            responseData = this.bindBankCardService.bindBankCardApplyCallback(paramsMap, request.getQueryString());
        }else{
            responseData = this.bindBankCardService.bindBankCardCallback(paramsMap, request.getQueryString());
        }
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/mer_replace_card_notify",method = RequestMethod.GET)
    public ModelAndView replaceBankCard(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData;
        String service = paramsMap.get("service");
        if(UmPayService.NOTIFY_MER_BIND_CARD_APPLY.getServiceName().equals(service)){
            responseData = this.bindBankCardService.bindBankCardApplyCallback(paramsMap, request.getQueryString());
        } else {
            responseData = bindBankCardService.replaceBankCardCallback(paramsMap, request.getQueryString());
        }
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/mer_bind_card_apply_notify", method = RequestMethod.GET)
    public ModelAndView bindBankCardNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.bindBankCardService.bindBankCardCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/withdraw_notify", method = RequestMethod.GET)
    public ModelAndView withdrawNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.withdrawService.withdrawCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/invest_notify", method = RequestMethod.GET)
    public ModelAndView investNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.investService.investCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/repay_notify", method = RequestMethod.GET)
    public ModelAndView repayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.normalRepayService.repayCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/repay_payback_notify", method = RequestMethod.GET)
    public ModelAndView repayPaybackNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.normalRepayService.investPaybackCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/repay_invest_fee_notify", method = RequestMethod.GET)
    public ModelAndView repayInvestFeeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.normalRepayService.investFeeCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/advance_repay_notify", method = RequestMethod.GET)
    public ModelAndView advanceRepayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.advanceRepayService.repayCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/advance_repay_payback_notify", method = RequestMethod.GET)
    public ModelAndView advanceRepayPaybackNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.advanceRepayService.investPaybackCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/advance_repay_invest_fee_notify", method = RequestMethod.GET)
    public ModelAndView advanceRepayInvestFeeNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.advanceRepayService.investFeeCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/over_invest_payback_notify", method = RequestMethod.GET)
    public ModelAndView overInvestPaybackNotify(HttpServletRequest request) {
        //TODO:remove this log
        logger.info("into over_invest_payback_notify");
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.investService.overInvestPaybackCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @RequestMapping(value = "/cancel_pay_back_notify", method = RequestMethod.GET)
    public ModelAndView cancelPayBackNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = loanService.cancelPayBackCallback(paramsMap, request.getQueryString());
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
