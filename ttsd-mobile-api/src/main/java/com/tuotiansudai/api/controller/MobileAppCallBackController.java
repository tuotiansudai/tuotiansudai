package com.tuotiansudai.api.controller;

import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.UmPayFrontService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callBack(UmPayFrontService service, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/callBackTemplate");
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String retCode = paramsMap.get("ret_code");
        Map<String,String> retMaps = Maps.newHashMap();
        if ("0000".equals(retCode)) {
            retMaps = this.frontMessageByService(service,"success");
            mv.addObject("message", retMaps.get("message"));
            mv.addObject("href",retMaps.get("href"));
            mv.addObject("service", service);
        } else {
            retMaps = this.frontMessageByService(service,"fail");
            mv.addObject("message", retMaps.get("message"));
            mv.addObject("href",retMaps.get("href"));
            mv.addObject("service", service);
        }
        mv.addObject("service", service);
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

    private Map<String, String> frontMessageByService(UmPayFrontService service,String callBackStatus) {
        Map<String, String> retMaps = Maps.newHashMap();
        String message = "";
        String href = "";
        if (UmPayFrontService.CUST_WITHDRAWALS.equals(service)) {
            message = "success".equals(service)?"提现成功":"提现失败";
            href = MessageFormat.format("tuotian://withdraw/{0}",callBackStatus);

        } else if (UmPayFrontService.MER_RECHARGE_PERSON.equals(service)) {
            message = "success".equals(service)?"充值成功":"充值失败";
            href = MessageFormat.format("tuotian://recharge/{0}",callBackStatus);

        } else if (UmPayFrontService.PROJECT_TRANSFER_INVEST.equals(service)) {
            message = "success".equals(service)?"投资成功":"投资失败";
            href = MessageFormat.format("tuotian://invest/{0}",callBackStatus);

        } else if (UmPayFrontService.PTP_MER_BIND_AGREEMENT.equals(service)) {
            message = "success".equals(service)?"签约成功":"签约失败";
            href = MessageFormat.format("tuotian://sign/{0}",callBackStatus);

        } else if (UmPayFrontService.PTP_MER_BIND_CARD.equals(service)) {
            message = "success".equals(service)?"绑卡成功":"绑卡失败";
            href = MessageFormat.format("tuotian://bindcard/{0}",callBackStatus);

        } else if (UmPayFrontService.PTP_MER_REPLACE_CARD.equals(service)) {
            message = "success".equals(service)?"换卡成功":"换卡失败";
            href = MessageFormat.format("tuotian://changecard/{0}",callBackStatus);

        }
        retMaps.put("message",message);
        retMaps.put("href",href);
        return retMaps;

    }


}
