package com.tuotiansudai.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/v1.0")
public abstract class MobileAppBaseController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    protected String getLoginName() {
        Object objLoginName = httpServletRequest.getAttribute("currentLoginName");
        return objLoginName == null ? "" : String.valueOf(objLoginName);
    }
}
