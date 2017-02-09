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
import java.util.Enumeration;
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

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callBack(@PathVariable String service, HttpServletRequest request) {
        ModelAndView mv;

        switch (MobileFrontCallbackService.getService(service)){
            case PTP_MER_BIND_AGREEMENT:
            case PTP_MER_REPLACE_CARD:
            case MEMBERSHIP_PRIVILEGE_PURCHASE:
            case PTP_MER_NO_PASSWORD_INVEST:
                mv = new ModelAndView("/success");
                break;
            case PROJECT_TRANSFER_INVEST:
            case PROJECT_TRANSFER_NOPASSWORD_INVEST:
            case PROJECT_TRANSFER_TRANSFER:
            case PROJECT_TRANSFER_NOPASSWORD_TRANSFER:
            case PTP_MER_BIND_CARD:
            case MER_RECHARGE_PERSON:
            case CUST_WITHDRAWALS:
                mv = new ModelAndView("/success-info");
                break;
            default:
                mv = new ModelAndView("/success");
        }

        MobileFrontCallbackService mobileFrontCallbackService = MobileFrontCallbackService.getService(service);
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String amount = mobileFrontCallbackService.getServiceName().equalsIgnoreCase("project_transfer_no_password_invest")
                            || mobileFrontCallbackService.getServiceName().equalsIgnoreCase("project_transfer_no_password_transfer")? paramsMap.get("amount") : "";
        String retCode = paramsMap.get("ret_code");
        String orderId = paramsMap.get("order_id");
        Map<String, String> retMaps = Maps.newHashMap();
        if ("0000".equals(retCode)) {
            retMaps = this.frontMessageByService(mobileFrontCallbackService, true, "", orderId, amount);
            mv.addObject("bankName", retMaps.get("bankName"));
            mv.addObject("cardNumber", retMaps.get("cardNumber"));
            mv.addObject("rechargeAmount", retMaps.get("rechargeAmount"));
            mv.addObject("withdrawAmount", retMaps.get("withdrawAmount"));
            mv.addObject("investAmount", retMaps.get("investAmount"));
            mv.addObject("investName", retMaps.get("investName"));
            mv.addObject("loanId", retMaps.get("loanId"));
            mv.addObject("replaceCardContent", retMaps.get("replaceCardContent"));
            mv.addObject("purchaseMembershipContent", retMaps.get("purchaseMembershipContent"));
            mv.addObject("href", retMaps.get("href"));
        } else {
            String retMsg = paramsMap.get("ret_msg");
            retMaps = this.frontMessageByService(mobileFrontCallbackService, false, retMsg, orderId, amount);
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

    private Map<String, String> frontMessageByService(MobileFrontCallbackService service, boolean isCallbackSuccess, String retMsg, String orderId, String amount) {
        Map<String, String> retMaps = Maps.newHashMap();
        String bankName = "";
        String cardNumber = "";
        String rechargeAmount = "";
        String investAmount = "";
        String investName = "";
        String loanId = "";
        String withdrawAmount = "";
        String replaceCardContent = "";
        String purchaseMembershipContent = "";
        switch (service){
            case CUST_WITHDRAWALS:
                    WithdrawModel withdrawModel = withdrawService.findById(Long.parseLong(orderId));
                    bankName = BankCardUtil.getBankName(withdrawModel.getBankCard().getBankCode());
                    cardNumber = withdrawModel.getBankCard().getCardNumber();
                    withdrawAmount = AmountConverter.convertCentToString(withdrawModel.getAmount());
                    break;

            case MER_RECHARGE_PERSON:
                    RechargeModel rechargeModel = rechargeService.findRechargeById(Long.parseLong(orderId));
                    bankName = BankCardUtil.getBankName(rechargeModel.getBankCode());
                    cardNumber = bindBankCardService.getPassedBankCard(rechargeModel.getLoginName()).getCardNumber();
                    rechargeAmount = AmountConverter.convertCentToString(rechargeModel.getAmount());
                    break;
            case PROJECT_TRANSFER_INVEST :
                    InvestModel investModel = investService.findById(Long.parseLong(orderId));
                    LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
                    investAmount = AmountConverter.convertCentToString(investModel.getAmount());
                    investName = loanModel.getName();
                    loanId = String.valueOf(loanModel.getId());
                    break;
            case PROJECT_TRANSFER_TRANSFER:
                    InvestModel investModelTransfer = investService.findById(Long.parseLong(orderId));
                    LoanModel loanModelTransfer = loanService.findLoanById(investModelTransfer.getLoanId());
                    investAmount = AmountConverter.convertCentToString(investModelTransfer.getAmount());
                    investName = loanModelTransfer.getName();
                    loanId = String.valueOf(loanModelTransfer.getId());
                    break;
            case PROJECT_TRANSFER_NOPASSWORD_INVEST:
                    LoanModel loanModelNoPass = loanService.findLoanById(Long.parseLong(orderId));
                    investAmount = AmountConverter.convertCentToString(Long.parseLong(amount));
                    investName = loanModelNoPass.getName();
                    loanId = String.valueOf(loanModelNoPass.getId());
                    break;
            case PROJECT_TRANSFER_NOPASSWORD_TRANSFER:
                    LoanModel loanModelNoPassTransfer = loanService.findLoanById(Long.parseLong(orderId));
                    investAmount = AmountConverter.convertCentToString(Long.parseLong(amount));
                    investName = loanModelNoPassTransfer.getName();
                    loanId = String.valueOf(loanModelNoPassTransfer.getId());
                    break;
            case PTP_MER_BIND_CARD:
                    BankCardModel bankCardModel = bindBankCardService.getBankCardById(Long.parseLong(orderId));
                    cardNumber = bankCardModel.getCardNumber();
                    bankName = BankCardUtil.getBankName(bankCardModel.getBankCode());
                    break;
            case PTP_MER_REPLACE_CARD:
                    replaceCardContent = "换卡申请最快两个小时处理完成";
                    break;
        }

        retMaps.put("message", isCallbackSuccess ? service.getMessage() : retMsg);
        retMaps.put("href", service.getConfirmUrl(isCallbackSuccess));
        retMaps.put("bankName", bankName);
        retMaps.put("cardNumber", cardNumber);
        retMaps.put("rechargeAmount", rechargeAmount);
        retMaps.put("withdrawNumber", withdrawAmount);
        retMaps.put("investAmount", investAmount);
        retMaps.put("investName", investName);
        retMaps.put("loanId", loanId);
        retMaps.put("withdrawAmount", withdrawAmount);
        retMaps.put("replaceCardContent", replaceCardContent);
        retMaps.put("purchaseMembershipContent", purchaseMembershipContent);
        return retMaps;
    }
}
