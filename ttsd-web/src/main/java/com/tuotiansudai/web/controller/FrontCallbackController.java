package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
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

    @Autowired
    public FrontCallbackController(PayWrapperClient payWrapperClient, InvestService investService, LoanService loanService) {
        this.payWrapperClient = payWrapperClient;
        this.investService = investService;
        this.loanService = loanService;
    }

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView parseCallback(@PathVariable String service, HttpServletRequest request) {
        logger.info(MessageFormat.format("front callback url: {0}", request.getRequestURL()));

        Map<String, String> params = parseRequestParameters(request);

        PayDataDto data = new PayDataDto();
        data.setStatus(true);

        try {
            AsyncUmPayService asyncUmPayService = AsyncUmPayService.valueOf(service.toUpperCase());

            if (!Lists.newArrayList(AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD, AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD).contains(asyncUmPayService)) {
                data = payWrapperClient.validateFrontCallback(params).getData();
            }

            if (Strings.isNullOrEmpty(asyncUmPayService.getWebRetCallbackPath()) || (!data.getStatus() && Strings.isNullOrEmpty(data.getCode()))) {
                return new ModelAndView("/error/404");
            }

            ModelAndView modelAndView = new ModelAndView("/front-callback-success");
            modelAndView.addObject("error", data.getStatus() ? null : data.getMessage());
            modelAndView.addObject("service", asyncUmPayService.name());
            if (Lists.newArrayList(AsyncUmPayService.INVEST_PROJECT_TRANSFER, AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD).contains(asyncUmPayService)) {
                InvestModel investModel = investService.findById(Long.valueOf(params.get("order_id")));
                LoanModel loanModel = loanService.findLoanById(investModel.getLoanId());
                modelAndView.addObject("amount", AmountConverter.convertCentToString(investModel.getAmount()));
                modelAndView.addObject("loanName", loanModel.getName());
                modelAndView.addObject("loanId", loanModel.getId());
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
