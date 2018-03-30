package com.tuotiansudai.paywrapper.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.luxury.LuxuryStageRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;


@Controller
@RequestMapping(value = "/callback")
public class LuxuryStageCallbackController {

    private static Logger logger = Logger.getLogger(LuxuryStageCallbackController.class);

    private final LuxuryStageRepayService luxuryStageRepayService;

    @Autowired
    public LuxuryStageCallbackController(LuxuryStageRepayService luxuryStageRepayService) {
        this.luxuryStageRepayService = luxuryStageRepayService;
    }

    @RequestMapping(value = "/luxury_stage_repay_notify", method = RequestMethod.GET)
    public ModelAndView repayCallback(HttpServletRequest request) {
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String responseData = this.luxuryStageRepayService.repayCallback(paramsMap, request.getQueryString());
        return new ModelAndView("/callback_response", "content", responseData);
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
