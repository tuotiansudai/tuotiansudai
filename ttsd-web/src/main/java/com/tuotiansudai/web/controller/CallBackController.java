package com.tuotiansudai.web.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.util.FrontCallbackService;
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
public class CallBackController {

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callBack(@PathVariable String service, HttpServletRequest request) {
        ModelAndView mv = null;
        FrontCallbackService frontCallbackService = FrontCallbackService.getService(service);
        //临时解决方案
        if(frontCallbackService != null){
            mv = new ModelAndView("/success");
            Map<String, String> paramsMap = this.parseRequestParameters(request);
            String retCode = paramsMap.get("ret_code");
            Map<String, String> retMaps = Maps.newHashMap();
            if ("0000".equals(retCode)) {
                retMaps = this.frontMessageByService(frontCallbackService, true, "");
            } else {
                String retMsg = paramsMap.get("ret_msg");
                retMaps = this.frontMessageByService(frontCallbackService, false, retMsg);
            }
            mv.addObject("message", retMaps.get("message"));
            mv.addObject("service", service);
        }
        else{
           mv = new ModelAndView("redirect:/account");
        }
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

    private Map<String, String> frontMessageByService(FrontCallbackService service, boolean isCallbackSuccess, String retMsg) {
        Map<String, String> retMaps = Maps.newHashMap();
        retMaps.put("message", isCallbackSuccess ? service.getMessage() : retMsg);
        return retMaps;
    }

}
