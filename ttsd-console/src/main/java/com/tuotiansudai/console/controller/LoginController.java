package com.tuotiansudai.console.controller;

import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.spring.security.CaptchaHelper;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private CaptchaHelper captchaHelper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("/login");
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void loginCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String sessionIdOrDeviceId = request.getSession(false) != null ? request.getSession(false).getId() : null;
        int captchaWidth = 80;
        int captchaHeight = 34;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight,
                this.captchaHelper.getCaptcha(sessionIdOrDeviceId));
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        this.captchaHelper.storeCaptcha(captcha.getAnswer(), sessionIdOrDeviceId);
    }
}
