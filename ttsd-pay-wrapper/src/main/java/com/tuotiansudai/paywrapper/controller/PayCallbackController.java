package com.tuotiansudai.paywrapper.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.paywrapper.service.*;
import com.tuotiansudai.paywrapper.repository.model.UmPayService;
import com.tuotiansudai.paywrapper.service.AgreementService;
import com.tuotiansudai.paywrapper.service.BindBankCardService;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.RechargeService;
import com.tuotiansudai.paywrapper.service.WithdrawService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private NormalRepayService normalRepayService;

    @Autowired
    private AdvanceRepayService advanceRepayService;

    @Autowired
    private AgreementService agreementService;


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

    @RequestMapping(value = "/advance_repay_notify", method = RequestMethod.GET)
    public ModelAndView advanceRepayNotify(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.advanceRepayService.repayCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
    }

    @ResponseBody
    @RequestMapping(value = "/async_invest_notify", method = RequestMethod.GET)
    public BaseDto<BaseDataDto> asyncInvestNotify(HttpServletRequest request) {
        return this.investService.asyncInvestCallback();
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
