package com.tuotiansudai.api.controller.v1_0;

import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.v1_0.UmPayFrontService;
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
public class MobileAppCallBackController {

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ModelAndView callBack(@PathVariable String service, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/callBackTemplate");
        Map<String, String> paramsMap = this.parseRequestParameters(request);
        String retCode = paramsMap.get("ret_code");
        Map<String,String> retMaps = Maps.newHashMap();
        if ("0000".equals(retCode)) {
            retMaps = this.frontMessageByService(service,"success","");
            mv.addObject("message", retMaps.get("message"));
            mv.addObject("href",retMaps.get("href"));
        } else {
            String retMsg = paramsMap.get("ret_msg");
            retMaps = this.frontMessageByService(service,"fail",retMsg);
            mv.addObject("message", retMaps.get("message"));
            mv.addObject("href",retMaps.get("href"));
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

    private Map<String, String> frontMessageByService(String service,String callBackStatus,String retMsg) {
        Map<String, String> retMaps = Maps.newHashMap();
        String message = "";
        String href = "";
        if (UmPayFrontService.CUST_WITHDRAWALS.getServiceName().equals(service)) {
            message = "提现成功";
            href = MessageFormat.format("tuotian://withdraw/{0}",callBackStatus);

        } else if (UmPayFrontService.MER_RECHARGE_PERSON.getServiceName().equals(service)) {
            message = "充值成功";
            href = MessageFormat.format("tuotian://recharge/{0}",callBackStatus);

        } else if (UmPayFrontService.PROJECT_TRANSFER_INVEST.getServiceName().equals(service)) {
            message = "投资成功";
            href = MessageFormat.format("tuotian://invest/{0}",callBackStatus);

        } else if (UmPayFrontService.PTP_MER_BIND_AGREEMENT.getServiceName().equals(service)) {
            message = "签约成功";
            href = MessageFormat.format("tuotian://sign/{0}",callBackStatus);

        } else if (UmPayFrontService.PTP_MER_BIND_CARD.getServiceName().equals(service)) {
            message = "绑卡成功";
            href = MessageFormat.format("tuotian://bindcard/{0}",callBackStatus);

        } else if (UmPayFrontService.PTP_MER_REPLACE_CARD.getServiceName().equals(service)) {
            message = "换卡成功";
            href = MessageFormat.format("tuotian://changecard/{0}",callBackStatus);

        } else if (UmPayFrontService.PTP_MER_NO_PASSWORD_INVEST.getServiceName().equals(service)) {
            message = "开通无密投资成功";
            href = MessageFormat.format("tuotian://nopasswordinvest/{0}",callBackStatus);
        }
        if("fail".equals(callBackStatus)){
            message = retMsg;
        }
        retMaps.put("message",message);
        retMaps.put("href",href);
        return retMaps;

    }


}
