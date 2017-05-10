package com.tuotiansudai.web.controller;

import com.tuotiansudai.spring.security.CaptchaHelper;
import com.tuotiansudai.util.CaptchaGenerator;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private CaptchaHelper captchaHelper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest httpServletRequest,
                              @RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        ModelAndView defaultModelAndView = new ModelAndView("/login", "redirect", redirect);
        defaultModelAndView.addObject("responsive", true);

        ModelAndView weChatModelAndView = new ModelAndView(MessageFormat.format("redirect:/wechat/wechat-entry-point?redirect={0}", redirect));

        return httpServletRequest.getSession().getAttribute("weChatUserLoginName") == null ? defaultModelAndView : weChatModelAndView;
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void loginCaptcha(HttpServletRequest request, HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        this.captchaHelper.storeCaptcha(captcha.getAnswer(), request.getSession().getId());
    }
}
