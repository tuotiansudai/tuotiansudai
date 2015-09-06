package com.tuotiansudai.web.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    private HttpServletRequest request;

    protected void setErrorMessage(int code, String message) {
        request.setAttribute("hasError", true);
        request.setAttribute("errorCode", code);
        request.setAttribute("errorMessage", message);
    }
}
