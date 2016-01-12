package com.tuotiansudai.web.controller;

import com.tuotiansudai.service.ImpersonateService;
import org.apache.ibatis.annotations.Param;
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

    @RequestMapping(params = {"loginName", "randomCode"})
    public ModelAndView impersonate(String loginName, String randomCode, HttpServletRequest request) {

        if (impersonateService.impersonateLogin(loginName, randomCode)) {
            request.getSession().setAttribute("impersonate", "1");
        }
        return new ModelAndView("redirect:/");
    }
}
