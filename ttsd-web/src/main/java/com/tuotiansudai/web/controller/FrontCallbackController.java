package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
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

    @Autowired
    public FrontCallbackController(PayWrapperClient payWrapperClient) {
        this.payWrapperClient = payWrapperClient;
    }

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView parseCallback(@PathVariable String service, HttpServletRequest request) {
        logger.info(MessageFormat.format("front callback url: {0}", request.getRequestURL()));

        PayDataDto data = payWrapperClient.validateFrontCallback(parseRequestParameters(request)).getData();
        if (!data.getStatus() && Strings.isNullOrEmpty(data.getCode())) {
            return new ModelAndView("/error/404");
        }

        try {
            AsyncUmPayService asyncUmPayService = AsyncUmPayService.valueOf(service);
            if (Strings.isNullOrEmpty(asyncUmPayService.getWebRetCallbackPath())) {
                return new ModelAndView("/error/404");
            }

            ModelAndView modelAndView = new ModelAndView("/front-callback-success");
            modelAndView.addObject("error", data.getStatus() ? null : data.getMessage());
            modelAndView.addObject("service", asyncUmPayService.getServiceName());

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
