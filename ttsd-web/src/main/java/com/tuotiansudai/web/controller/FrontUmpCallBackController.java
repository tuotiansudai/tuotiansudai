package com.tuotiansudai.web.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.enums.UmpCallbackType;
import org.apache.log4j.Logger;
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
@RequestMapping(path = "/ump/return-url/callback")
public class FrontUmpCallBackController {

    static Logger logger = Logger.getLogger(FrontUmpCallBackController.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @RequestMapping(value = "/{umpCallbackType}", method = RequestMethod.GET)
    public ModelAndView parseCallback(@PathVariable UmpCallbackType umpCallbackType, HttpServletRequest request) {
        logger.info(MessageFormat.format("ump front callback url: {0}", request.getRequestURL()));
        Map<String, String> params = parseRequestParameters(request);
        Boolean isSuccess = bankWrapperClient.isUmpCallbackSuccess(params);
        if (isSuccess == null || !isSuccess){
            return new ModelAndView("/error/404");
        }
        return new ModelAndView("/ump-front-callback-success").addObject("umpCallbackType", umpCallbackType);
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
