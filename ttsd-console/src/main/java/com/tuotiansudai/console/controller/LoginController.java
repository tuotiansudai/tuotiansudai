package com.tuotiansudai.console.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2015/8/14.
 */
@Controller
@RequestMapping(value = "/admin/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);
}
