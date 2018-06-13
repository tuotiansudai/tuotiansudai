package com.tuotiansudai.api.controller;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
import com.tuotiansudai.util.RedisWrapperClient;
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
import java.util.function.Function;

@Controller
@RequestMapping(value = "/callback")
public class MobileAppFrontCallBackController {

    private static Logger logger = Logger.getLogger(MobileAppFrontCallBackController.class);

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private BankRechargeService bankRechargeService;

    @Autowired
    private BankWithdrawService bankWithdrawService;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanService loanService;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String HUIZU_ACTIVE_RECHARGE_KEY = "huizu_active_recharge:";

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callback(@PathVariable String service, HttpServletRequest request) {
        logger.info(MessageFormat.format("mobile front callback url: {0}", request.getRequestURL()));

        Map<String, String> params = parseRequestParameters(request);

        AsyncUmPayService asyncUmPayService = AsyncUmPayService.valueOf(service.toUpperCase());

        PayDataDto data = new PayDataDto();
        data.setStatus(true);


        ModelAndView modelAndView = new ModelAndView("/front-callback", "message", data.getMessage());

        if (!data.getStatus() && Strings.isNullOrEmpty(data.getCode())) {
            modelAndView.addObject("message", MessageFormat.format(asyncUmPayService.getMobileLink(), "fail"));
            modelAndView.addObject("href", MessageFormat.format(asyncUmPayService.getMobileLink(), "fail"));
            return modelAndView;
        }

        modelAndView.addObject("service", service);
        String orderId = params.get("order_id");
        modelAndView.addObject("values", this.generateBindValues(asyncUmPayService, Strings.isNullOrEmpty(orderId) ? null : Long.valueOf(params.get("order_id"))));

        String mobileLink = asyncUmPayService.getMobileLink();
        if (AsyncUmPayService.MER_RECHARGE_PERSON.equals(asyncUmPayService)) {
            if (redisWrapperClient.exists(HUIZU_ACTIVE_RECHARGE_KEY + orderId)) {
                mobileLink = mobileLink.replace("recharge", "huizu_active_recharge");
            }
        }

        modelAndView.addObject("href", MessageFormat.format(mobileLink, "success"));
        return modelAndView;
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

    private Map<String, String> generateBindValues(AsyncUmPayService service, Long orderId) {
        Function<Long, Map<String, String>> bindCardValuesGenerator = (Long bindCardOrderId) -> {
            BankCardModel bankCardModel = bindCardOrderId != null ? bindBankCardService.getBankCardById(bindCardOrderId) : null;
            return Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("cardNumber", bankCardModel != null ? bankCardModel.getCardNumber().replaceAll("^(\\d{4}).*(\\d{4})$", "$1****$2") : "")
                    .put("message", "绑卡申请成功")
                    .build());
        };

        Function<Long, Map<String, String>> replaceCardValuesGenerator = (Long replaceCardOrderId) -> {
            BankCardModel bankCardModel = replaceCardOrderId != null ? bindBankCardService.getBankCardById(replaceCardOrderId) : null;
            return Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("message", "换卡申请成功")
                    .put("manual", bankCardModel != null ? String.valueOf(bindBankCardService.isManual(bankCardModel.getLoginName())) : "false")
                    .build());
        };


        Function<Long, Map<String, String>> rechargeValuesGenerator = (Long rechargeOrderId) -> {
            BankRechargeModel bankRechargeModel = rechargeOrderId != null ? bankRechargeService.findRechargeById(rechargeOrderId) : null;
            return Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("cardNumber", bankRechargeModel != null ? bindBankCardService.getPassedBankCard(bankRechargeModel.getLoginName()).getCardNumber().replaceAll("^(\\d{4}).*(\\d{4})$", "$1****$2") : "")
                    .put("rechargeAmount", bankRechargeModel != null ? AmountConverter.convertCentToString(bankRechargeModel.getAmount()) : "")
                    .put("orderId", String.valueOf(rechargeOrderId))
                    .put("message", "充值成功")
                    .build());
        };

        Function<Long, Map<String, String>> withdrawValuesGenerator = (Long withdrawOrderId) -> {
            BankWithdrawModel bankWithdrawModel = withdrawOrderId != null ? bankWithdrawService.findById(withdrawOrderId) : null;
            return Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("withdrawAmount", (bankWithdrawModel != null ? AmountConverter.convertCentToString(bankWithdrawModel.getAmount()) : ""))
                    .put("orderId", String.valueOf(withdrawOrderId))
                    .put("message", "提现申请成功")
                    .build());
        };

        Function<Long, Map<String, String>> investValuesGenerator = (Long investId) -> {
            InvestModel investModel = investId != null ? investService.findById(investId) : null;
            return Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("loanName", investModel != null ? loanService.findLoanById(investModel.getLoanId()).getName() : "")
                    .put("investAmount", (investModel != null ? AmountConverter.convertCentToString(investModel.getAmount()) : ""))
                    .put("loanId", investModel != null ? String.valueOf(investModel.getLoanId()) : "")
                    .put("message", Lists.newArrayList(AsyncUmPayService.INVEST_PROJECT_TRANSFER, AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD).contains(service) ? "投资成功" : "债权购买成功")
                    .build());
        };

        Function<Long, Map<String, String>> memberPrivilegePurchaseValuesGenerator = (Long replaceCardOrderId) -> Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("message", "成功购买增值特权")
                .build());

        Function<Long, Map<String, String>> bindAgreementValuesGenerator = (Long replaceCardOrderId) -> Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("message", "签约成功")
                .build());

        Map<AsyncUmPayService, Function<Long, Map<String, String>>> generatorMapper = Maps.newHashMap(ImmutableMap.<AsyncUmPayService, Function<Long, Map<String, String>>>builder()
                .put(AsyncUmPayService.PTP_MER_BIND_CARD, bindCardValuesGenerator)
                .put(AsyncUmPayService.PTP_MER_REPLACE_CARD, replaceCardValuesGenerator)
                .put(AsyncUmPayService.MER_RECHARGE_PERSON, rechargeValuesGenerator)
                .put(AsyncUmPayService.CUST_WITHDRAWALS, withdrawValuesGenerator)
                .put(AsyncUmPayService.INVEST_PROJECT_TRANSFER, investValuesGenerator)
                .put(AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD, investValuesGenerator)
                .put(AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER, investValuesGenerator)
                .put(AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD, investValuesGenerator)
                .put(AsyncUmPayService.MEMBERSHIP_PRIVILEGE_PURCHASE_TRANSFER_ASYN, memberPrivilegePurchaseValuesGenerator)
                .put(AsyncUmPayService.NO_PASSWORD_INVEST_PTP_MER_BIND_AGREEMENT, bindAgreementValuesGenerator)
                .put(AsyncUmPayService.AUTO_REPAY_PTP_MER_BIND_AGREEMENT, bindAgreementValuesGenerator)
                .put(AsyncUmPayService.FAST_PAY_MER_BIND_AGREEMENT, bindAgreementValuesGenerator)
                .build());

        return generatorMapper.containsKey(service) ? generatorMapper.get(service).apply(orderId) : Maps.newHashMap();
    }
}
