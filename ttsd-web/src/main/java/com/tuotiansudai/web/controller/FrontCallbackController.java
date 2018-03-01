package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
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
public class FrontCallbackController {

    static Logger logger = Logger.getLogger(FrontCallbackController.class);

    private final PayWrapperClient payWrapperClient;

    private final InvestService investService;

    private final LoanService loanService;

    private final WithdrawService withdrawService;

    private final RechargeService rechargeService;

    private final BindBankCardService bindBankCardService;

    private final BankCardMapper bankCardMapper;

    @Autowired
    public FrontCallbackController(PayWrapperClient payWrapperClient,
                                   InvestService investService,
                                   LoanService loanService,
                                   WithdrawService withdrawService,
                                   RechargeService rechargeService,
                                   BindBankCardService bindBankCardService,
                                   BankCardMapper bankCardMapper) {
        this.payWrapperClient = payWrapperClient;
        this.investService = investService;
        this.loanService = loanService;
        this.withdrawService = withdrawService;
        this.rechargeService = rechargeService;
        this.bindBankCardService = bindBankCardService;
        this.bankCardMapper = bankCardMapper;
    }


    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView parseCallback(@PathVariable String service, HttpServletRequest request) {
        logger.info(MessageFormat.format("front callback url: {0}", request.getRequestURL()));

        Map<String, String> params = parseRequestParameters(request);

        PayDataDto data = new PayDataDto();
        data.setStatus(true);

        try {
            AsyncUmPayService asyncUmPayService = AsyncUmPayService.valueOf(service.toUpperCase());

            if (!Lists.newArrayList(AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD,
                    AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD,
                    AsyncUmPayService.CUST_WITHDRAWALS).contains(asyncUmPayService)) {
                data = payWrapperClient.validateFrontCallback(params).getData();
            }

            if (Strings.isNullOrEmpty(asyncUmPayService.getWebRetCallbackPath()) || (!data.getStatus() && Strings.isNullOrEmpty(data.getCode()))) {
                return new ModelAndView("/error/404");
            }

            ModelAndView modelAndView = new ModelAndView("/front-callback-success");
            modelAndView.addObject("error", data.getStatus() ? null : data.getMessage());
            modelAndView.addObject("service", asyncUmPayService.name());

            if (AsyncUmPayService.PTP_MER_BIND_CARD == asyncUmPayService) {
                Long orderId = Long.valueOf(params.get("order_id"));
                BankCardModel bankCardModel = bankCardMapper.findById(orderId);
                modelAndView.addObject("cardNumber", bankCardModel != null ? bankCardModel.getCardNumber() : "");
            }

            if (Lists.newArrayList(
                    AsyncUmPayService.INVEST_PROJECT_TRANSFER, AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD,
                    AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER, AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD
            ).contains(asyncUmPayService)) {
                InvestModel investModel = investService.findById(Long.valueOf(params.get("order_id")));
                LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
                modelAndView.addObject("amount", AmountConverter.convertCentToString(investModel.getAmount()));
                modelAndView.addObject("loanName", loanModel.getName());
                modelAndView.addObject("loanId", loanModel.getId());
                modelAndView.addObject("serviceName", "投资成功");
            }
            if (AsyncUmPayService.CUST_WITHDRAWALS == asyncUmPayService) {
                WithdrawModel withdrawModel = withdrawService.findById(Long.valueOf(params.get("order_id")));
                BankCardModel bankCardModel = bindBankCardService.getBankCardById(withdrawModel.getBankCardId());
                modelAndView.addObject("amount", AmountConverter.convertCentToString(withdrawModel.getAmount()));
                modelAndView.addObject("cardNumber", bankCardModel.getCardNumber());
                modelAndView.addObject("bankName", BankCardUtil.getBankName(bankCardModel.getBankCode()));
                modelAndView.addObject("orderId", params.get("order_id"));
                modelAndView.addObject("serviceName", "申请提现成功");
            }
            if (AsyncUmPayService.MER_RECHARGE_PERSON == asyncUmPayService) {
                RechargeModel rechargeModel = rechargeService.findRechargeById(Long.valueOf(params.get("order_id")));
                BankCardModel bankCard = bindBankCardService.getPassedBankCard(rechargeModel.getLoginName());
                modelAndView.addObject("amount", AmountConverter.convertCentToString(rechargeModel.getAmount()));
                modelAndView.addObject("cardNumber", bankCard != null ? bankCard.getCardNumber() : "");
                modelAndView.addObject("bankName", bankCard != null ? BankCardUtil.getBankName(bankCard.getBankCode()) : "");
                modelAndView.addObject("orderId", params.get("order_id"));
                modelAndView.addObject("serviceName", "充值成功");
            }

            return modelAndView;
        } catch (IllegalArgumentException e) {
            logger.warn(MessageFormat.format("callback service({0}) is not exist", service));
            return new ModelAndView("/error/404");
        }
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
