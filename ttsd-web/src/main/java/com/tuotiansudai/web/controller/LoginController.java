package com.tuotiansudai.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);

    @Value(value = "${web.domain:#{null}}")
    private String domain;

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private SignInClient signInClient;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        ModelAndView modelAndView = new ModelAndView("/login", "redirect", redirect);
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public LoginDto login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        String captcha = httpServletRequest.getParameter("captcha");
        SignInDto signInDto = new SignInDto(username, password, captcha, Source.WEB.name(), null);
        LoginDto loginDto = signInClient.sendSignIn(httpServletRequest.getSession().getId(), signInDto);
        httpServletResponse.addCookie(this.createSessionCookie(httpServletRequest, loginDto));
        System.out.println("====login===" + objectMapper.writeValueAsString(loginDto));
        return loginDto;
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

    private Cookie createSessionCookie(HttpServletRequest request, LoginDto loginDto) {
        String sessionId = loginDto.getNewSessionId() != null ? loginDto.getNewSessionId() : request.getSession().getId();
        Cookie cookie = new Cookie("SESSION", sessionId);
        cookie.setSecure(request.isSecure());
        cookie.setPath(MessageFormat.format("{0}/", request.getContextPath()));
        if (!Strings.isNullOrEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setHttpOnly(this.isServlet3());

        return cookie;
    }

    private boolean isServlet3() {
        try {
            ServletRequest.class.getMethod("startAsync");
            return true;
        } catch (NoSuchMethodException ignored) {
            logger.error(ignored.getLocalizedMessage(), ignored);
        }
        return false;
    }
}
