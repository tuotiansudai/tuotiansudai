package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private SignInClient signInClient;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        ModelAndView modelAndView = new ModelAndView("/login", "redirect", redirect);
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<LoginDto> loginIn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        String captcha = httpServletRequest.getParameter("captcha");
        SignInDto signInDto = new SignInDto(username, password, captcha, Source.WEB.name(), null);
        BaseDto<LoginDto> baseDto = signInClient.sendSignIn(httpServletRequest.getSession().getId(), signInDto);
        Map<String, String> sessionIds = new HashMap<>();
        sessionIds.put("SESSION", baseDto.getData().getNewSessionId());
        Cookie cookie = signInClient.createSessionCookie(httpServletRequest, sessionIds);
        httpServletResponse.addCookie(cookie);
        return baseDto;
    }

    @RequestMapping(value = "/sign-out", method = RequestMethod.POST)
    public ModelAndView loginOut(HttpServletRequest httpServletRequest) {
        signInClient.sendSignOut(httpServletRequest.getSession().getId());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void loginCaptcha(HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        this.captchaHelper.storeCaptcha(CaptchaHelper.LOGIN_CAPTCHA, captcha.getAnswer());
    }
}
