package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ImpersonateService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequestMapping(value = "/impersonate")
public class ImpersonateController {

    @Autowired
    private ImpersonateService impersonateService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @RequestMapping(params = "securityCode")
    public ModelAndView impersonate(String securityCode, HttpServletRequest request, HttpServletResponse response) {
        String loginName = impersonateService.impersonateLogin(securityCode);
        if (Strings.isNullOrEmpty(loginName)) {
            myAuthenticationUtil.createAuthentication(loginName, Source.WEB);
            request.getSession().setAttribute("impersonate", "1");
            return new ModelAndView("redirect:/");
        } else {
            response.setStatus(404);
            return new ModelAndView("/error/404");
        }
    }
}
