package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ImpersonateService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Component
@RequestMapping(path = "/impersonate")
public class ImpersonateController {

    private final MyAuthenticationUtil myAuthenticationUtil = MyAuthenticationUtil.getInstance();

    private final ImpersonateService impersonateService;

    @Autowired
    public ImpersonateController(ImpersonateService impersonateService) {
        this.impersonateService = impersonateService;
    }


    @RequestMapping(path = "/security-code/{securityCode}")
    public ModelAndView impersonate(HttpServletRequest request, @PathVariable String securityCode) {
        String loginName = impersonateService.impersonateLogin(securityCode);
        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView("/error/404");
        }
        myAuthenticationUtil.createAuthentication(loginName, Source.WEB, request.getHeader("X-Forwarded-For"));
        request.getSession().setAttribute("impersonate", "1");
        return new ModelAndView("redirect:/");
    }
}
