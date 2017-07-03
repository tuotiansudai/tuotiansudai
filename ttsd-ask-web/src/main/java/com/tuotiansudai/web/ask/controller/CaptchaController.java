package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.ask.service.CaptchaHelperService;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaHelperService captchaHelper;

    @RequestMapping(method = RequestMethod.GET)
    public void generateCaptcha(HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight, null);
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        this.captchaHelper.storeCaptcha(captcha.getAnswer());
    }
}
