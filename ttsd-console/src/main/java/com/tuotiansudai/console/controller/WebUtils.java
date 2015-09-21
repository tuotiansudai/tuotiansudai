package com.tuotiansudai.console.controller;

import org.springframework.web.servlet.ModelAndView;

public class WebUtils {
    private static final String ERR_CODE_KEY = "TTSD_ERROR_CODE";
    private static final String ERR_MSG_KEY = "TTSD_MSG_CODE";
    public static ModelAndView addError(ModelAndView mv, String errCode, String errMessage){
        mv.addObject(ERR_CODE_KEY, errCode);
        mv.addObject(ERR_MSG_KEY, errMessage);
        return mv;
    }

    public static ModelAndView addError(ModelAndView mv, Throwable cause){
        return addError(mv, cause.getClass().getName(), cause.getMessage());
    }
}
