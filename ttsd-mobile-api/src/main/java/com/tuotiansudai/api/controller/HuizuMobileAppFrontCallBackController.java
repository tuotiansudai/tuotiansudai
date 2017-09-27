package com.tuotiansudai.api.controller;

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
@RequestMapping(value = "/huizu/callback")
public class HuizuMobileAppFrontCallBackController {

    private static Logger logger = Logger.getLogger(HuizuMobileAppFrontCallBackController.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callback(@PathVariable String service, HttpServletRequest request) {
        logger.info(MessageFormat.format("huizu mobile front callback url: {0}", request.getRequestURL()));

        Map<String, String> params = parseRequestParameters(request);

        AsyncUmPayService asyncUmPayService = AsyncUmPayService.valueOf(service.toUpperCase());

        PayDataDto data = new PayDataDto();
        data.setStatus(true);

        data = payWrapperClient.validateFrontCallback(params).getData();

        ModelAndView modelAndView = new ModelAndView("/huizu-front-callback", "message", data.getMessage());

        if (!data.getStatus() && Strings.isNullOrEmpty(data.getCode())) {
            modelAndView.addObject("message", MessageFormat.format(asyncUmPayService.getMobileLink(), "fail"));
            modelAndView.addObject("href", MessageFormat.format(asyncUmPayService.getMobileLink(), "fail"));
            return modelAndView;
        }

        modelAndView.addObject("service", service);
        modelAndView.addObject("href", MessageFormat.format(asyncUmPayService.getMobileLink(), "success"));
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
}
