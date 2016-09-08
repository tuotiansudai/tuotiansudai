package com.tuotiansudai.api.controller.v1_0;

import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.v1_0.UmPayFrontService;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Map;

@Controller
@RequestMapping(value = "/callback")
public class MobileAppCallBackController {

    static Logger logger = Logger.getLogger(MobileAppCallBackController.class);

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

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callBack(@PathVariable String service, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/callBackTemplate");
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String retCode = paramsMap.get("ret_code");
        String orderId = paramsMap.get("order_id");
        String investAmount = service.equals("project_transfer_no_password_invest")? paramsMap.get("investAmount"):"";

        Map<String,String> retMaps = Maps.newHashMap();
        if ("0000".equals(retCode)) {
            retMaps = this.frontMessageByService(service,"success","",orderId,investAmount);
            mv.addObject("bankName", retMaps.get("bankName"));
            mv.addObject("cardNumber",retMaps.get("cardNumber"));
            mv.addObject("rechargeAmount", retMaps.get("rechargeAmount"));
            mv.addObject("withdrawAmount",retMaps.get("withdrawAmount"));
            mv.addObject("investAmount",retMaps.get("investAmount"));
            mv.addObject("investName",retMaps.get("investName"));
            mv.addObject("investId",retMaps.get("investId"));
            mv.addObject("href",retMaps.get("href"));
        } else {
            String retMsg = paramsMap.get("ret_msg");
            retMaps = this.frontMessageByService(service,"fail",retMsg,orderId,investAmount);
            mv.addObject("href",retMaps.get("href"));
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

    private Map<String, String> frontMessageByService(String service,String callBackStatus,String retMsg,String orderId,String investAmount) {
        Map<String, String> retMaps = Maps.newHashMap();
        String message = "";
        String href = "";
        String bankName = "";
        String cardNumber = "";
        String rechargeAmount = "";
        String investAmount = "";
        String investName = "";
        String investId = "";
        String withdrawAmount = "";
        if (UmPayFrontService.CUST_WITHDRAWALS.getServiceName().equals(service)) {
            WithdrawModel withdrawModel = withdrawService.findById(Long.parseLong(orderId));
            bankName = BankCardUtil.getBankName(withdrawModel.getBankCard().getBankCode());
            cardNumber = withdrawModel.getBankCard().getCardNumber();
            withdrawAmount = AmountConverter.convertCentToString(withdrawModel.getAmount());
            message = "申请提现成功";
            href = MessageFormat.format("tuotian://withdraw/{0}",callBackStatus);
        } else if (UmPayFrontService.MER_RECHARGE_PERSON.getServiceName().equals(service)) {
            RechargeModel rechargeModel = rechargeService.findRechargeById(Long.parseLong(orderId));
            bankName = BankCardUtil.getBankName(rechargeModel.getBankCode());
            cardNumber = bindBankCardService.getPassedBankCard(rechargeModel.getLoginName()).getCardNumber();
            rechargeAmount = AmountConverter.convertCentToString(rechargeModel.getAmount());
            message = "充值成功";
            href = MessageFormat.format("tuotian://recharge/{0}",callBackStatus);
        } else if (UmPayFrontService.PROJECT_TRANSFER_INVEST.getServiceName().equals(service)) {
            InvestModel investModel = investService.findById(Long.parseLong(orderId));
            LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
            investAmount = AmountConverter.convertCentToString(investModel.getAmount());
            investName = loanModel.getName();
            investId = String.valueOf(loanModel.getId());
            message = "投资成功";
            href = MessageFormat.format("tuotian://invest/{0}",callBackStatus);
        }
        else if (UmPayFrontService.PROJECT_TRANSFER_NOPASSWORD_INVEST.getServiceName().equals(service)) {
            LoanModel loanModel = loanService.findLoanById(Long.parseLong(orderId));
            investAmount = AmountConverter.convertCentToString(Long.parseLong(investAmount));
            investName = loanModel.getName();
            investId = String.valueOf(loanModel.getId());
            message = "投资成功";
            href = MessageFormat.format("tuotian://invest/{0}",callBackStatus);
        } else if (UmPayFrontService.PTP_MER_BIND_AGREEMENT.getServiceName().equals(service)) {
            message = "签约成功";
            href = MessageFormat.format("tuotian://sign/{0}",callBackStatus);
        } else if (UmPayFrontService.PTP_MER_BIND_CARD.getServiceName().equals(service)) {
            BankCardModel bankCardModel =  bindBankCardService.getPassedBankCardById(Long.parseLong(orderId));
            cardNumber = bankCardModel.getCardNumber();
            bankName = BankCardUtil.getBankName(bankCardModel.getBankCode().toUpperCase());
            message = "绑卡成功";
            href = MessageFormat.format("tuotian://bindcard/{0}",callBackStatus);
        } else if (UmPayFrontService.PTP_MER_REPLACE_CARD.getServiceName().equals(service)) {
            message = "换卡成功";
            href = MessageFormat.format("tuotian://changecard/{0}",callBackStatus);
        } else if (UmPayFrontService.PTP_MER_NO_PASSWORD_INVEST.getServiceName().equals(service)) {
            message = "开通无密投资成功";
            href = MessageFormat.format("tuotian://nopasswordinvest/{0}",callBackStatus);
        }
        if("fail".equals(callBackStatus)){
            message = retMsg;
        }
        retMaps.put("message",message);
        retMaps.put("href",href);
        retMaps.put("bankName",bankName);
        retMaps.put("cardNumber",cardNumber);
        retMaps.put("rechargeAmount",rechargeAmount);
        retMaps.put("withdrawNumber",withdrawAmount);
        retMaps.put("investAmount",investAmount);
        retMaps.put("investName",investName);
        retMaps.put("investId",investId);
        retMaps.put("withdrawAmount",withdrawAmount);
        return retMaps;

    }


}
