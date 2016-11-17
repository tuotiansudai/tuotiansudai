package com.tuotiansudai.api.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
import com.tuotiansudai.util.MobileFrontCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/callback")
public class MobileAppCallBackController {

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/membership-purchase", method = RequestMethod.GET)
    public ModelAndView callBack(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/callback-template");
        mv.addAllObjects(this.generateViewObject(MobileFrontCallbackService.MEMBERSHIP_PURCHASE, request));
        return mv;
    }

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callBack(@PathVariable String service, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/callBackTemplate");
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String retCode = paramsMap.get("ret_code");
        String orderId = paramsMap.get("order_id");
        String amount = service.equals("project_transfer_no_password_invest") ? paramsMap.get("amount") : "";
        Map<String, String> retMaps = Maps.newHashMap();
        if ("0000".equals(retCode)) {
            retMaps = this.frontMessageByService(service, "success", "", orderId, amount);
            mv.addObject("bankName", retMaps.get("bankName"));
            mv.addObject("cardNumber", retMaps.get("cardNumber"));
            mv.addObject("rechargeAmount", retMaps.get("rechargeAmount"));
            mv.addObject("withdrawAmount", retMaps.get("withdrawAmount"));
            mv.addObject("investAmount", retMaps.get("investAmount"));
            mv.addObject("investName", retMaps.get("investName"));
            mv.addObject("loanId", retMaps.get("loanId"));
            mv.addObject("replaceCardContent", retMaps.get("replaceCardContent"));
            mv.addObject("href", retMaps.get("href"));
        } else {
            String retMsg = paramsMap.get("ret_msg");
            retMaps = this.frontMessageByService(service, "fail", retMsg, orderId, amount);
            mv.addObject("href", retMaps.get("href"));
        }
        mv.addObject("message", retMaps.get("message"));
        mv.addObject("service", service);
        mv.addObject("orderId", orderId);
        return mv;
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

    private Map<String, String> frontMessageByService(String service, String callBackStatus, String retMsg, String orderId, String amount) {
        Map<String, String> retMaps = Maps.newHashMap();
        String message = "";
        String href = "";
        String bankName = "";
        String cardNumber = "";
        String rechargeAmount = "";
        String investAmount = "";
        String investName = "";
        String loanId = "";
        String withdrawAmount = "";
        String replaceCardContent = "";
        if (MobileFrontCallbackService.CUST_WITHDRAWALS.getServiceName().equals(service)) {
            WithdrawModel withdrawModel = withdrawService.findById(Long.parseLong(orderId));
            bankName = BankCardUtil.getBankName(withdrawModel.getBankCard().getBankCode());
            cardNumber = withdrawModel.getBankCard().getCardNumber();
            withdrawAmount = AmountConverter.convertCentToString(withdrawModel.getAmount());
            message = "申请提现成功";
            href = MessageFormat.format("tuotian://withdraw/{0}", callBackStatus);
        } else if (MobileFrontCallbackService.MER_RECHARGE_PERSON.getServiceName().equals(service)) {
            RechargeModel rechargeModel = rechargeService.findRechargeById(Long.parseLong(orderId));
            bankName = BankCardUtil.getBankName(rechargeModel.getBankCode());
            cardNumber = bindBankCardService.getPassedBankCard(rechargeModel.getLoginName()).getCardNumber();
            rechargeAmount = AmountConverter.convertCentToString(rechargeModel.getAmount());
            message = "充值成功";
            href = MessageFormat.format("tuotian://recharge/{0}", callBackStatus);
        } else if (MobileFrontCallbackService.PROJECT_TRANSFER_INVEST.getServiceName().equals(service)) {
            InvestModel investModel = investService.findById(Long.parseLong(orderId));
            LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
            investAmount = AmountConverter.convertCentToString(investModel.getAmount());
            investName = loanModel.getName();
            loanId = String.valueOf(loanModel.getId());
            message = "投资成功";
            href = MessageFormat.format("tuotian://invest/{0}", callBackStatus);
        } else if (MobileFrontCallbackService.PROJECT_TRANSFER_NOPASSWORD_INVEST.getServiceName().equals(service)) {
            LoanModel loanModel = loanService.findLoanById(Long.parseLong(orderId));
            investAmount = AmountConverter.convertCentToString(Long.parseLong(amount));
            investName = loanModel.getName();
            loanId = String.valueOf(loanModel.getId());
            message = "投资成功";
            href = MessageFormat.format("tuotian://invest/{0}", callBackStatus);
        } else if (MobileFrontCallbackService.PTP_MER_BIND_AGREEMENT.getServiceName().equals(service)) {
            message = "签约成功";
            href = MessageFormat.format("tuotian://sign/{0}", callBackStatus);
        } else if (MobileFrontCallbackService.PTP_MER_BIND_CARD.getServiceName().equals(service)) {
            BankCardModel bankCardModel = bindBankCardService.getBankCardById(Long.parseLong(orderId));
            cardNumber = bankCardModel.getCardNumber();
            message = "绑卡申请成功";
            href = MessageFormat.format("tuotian://bindcard/{0}", callBackStatus);
        } else if (MobileFrontCallbackService.PTP_MER_REPLACE_CARD.getServiceName().equals(service)) {
            message = "换卡申请已提交";
            replaceCardContent =  "换卡申请最快两个小时处理完成";
            href = MessageFormat.format("tuotian://changecard/{0}", callBackStatus);
        } else if (MobileFrontCallbackService.PTP_MER_NO_PASSWORD_INVEST.getServiceName().equals(service)) {
            message = "开通无密投资成功";
            href = MessageFormat.format("tuotian://nopasswordinvest/{0}", callBackStatus);
        }
        if ("fail".equals(callBackStatus)) {
            message = retMsg;
        }
        retMaps.put("message", message);
        retMaps.put("href", href);
        retMaps.put("bankName", bankName);
        retMaps.put("cardNumber", cardNumber);
        retMaps.put("rechargeAmount", rechargeAmount);
        retMaps.put("withdrawNumber", withdrawAmount);
        retMaps.put("investAmount", investAmount);
        retMaps.put("investName", investName);
        retMaps.put("loanId", loanId);
        retMaps.put("withdrawAmount", withdrawAmount);
        retMaps.put("replaceCardContent",replaceCardContent);
        return retMaps;
    }

    private Map<String, Object> generateViewObject(MobileFrontCallbackService service, HttpServletRequest httpServletRequest) {
        Map<String, String> paramsMap = this.parseRequestParameters(httpServletRequest);
        boolean isCallbackSuccess = "0000".equalsIgnoreCase(paramsMap.get("ret_code"));
        HashMap<String, Object> viewObject = Maps.newHashMap();
        viewObject.put("href", service.getConfirmUrl(isCallbackSuccess));
        viewObject.put("message", isCallbackSuccess ? service.getMessage() : paramsMap.get("ret_msg"));
        return viewObject;
    }
}
