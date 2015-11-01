package com.tuotiansudai.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by CBJ on 2015/10/29.
 */
@Controller
public class ReferrerManageController {

    @RequestMapping(value = "/referrerManage", method = RequestMethod.GET)
    public ModelAndView referrerManage() {
        return new ModelAndView("/referrer-manage");
    }
}
