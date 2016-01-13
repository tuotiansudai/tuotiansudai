package com.tuotiansudai.web.controller;

import com.tuotiansudai.service.ImpersonateService;
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

    @RequestMapping(params = "securityCode")
    public ModelAndView impersonate(String securityCode, HttpServletRequest request, HttpServletResponse response) {

        if (impersonateService.impersonateLogin(securityCode)) {
            request.getSession().setAttribute("impersonate", "1");
            return new ModelAndView("redirect:/");
        } else {
            response.setStatus(404);
            return new ModelAndView("/error/404");
        }
    }
}
