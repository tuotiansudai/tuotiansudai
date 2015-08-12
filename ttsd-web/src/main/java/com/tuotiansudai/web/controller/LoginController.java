package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.security.CaptchaVerifier;
import com.tuotiansudai.service.UserService;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.renderer.DefaultWordRenderer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaVerifier captchaVerifier;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("/login");
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void loginCaptcha(HttpServletRequest request, HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha.Builder captchaBuilder = new Captcha.Builder(captchaWidth, captchaHeight);
        DefaultWordRenderer wordRenderer = new DefaultWordRenderer(Lists.newArrayList(Color.BLACK), Lists.newArrayList(new Font("Geneva", Font.BOLD, 24)));
        CurvedLineNoiseProducer noiseProducer = new CurvedLineNoiseProducer(Color.BLACK, 1.0f);
        Captcha captcha = captchaBuilder.addText(wordRenderer).addNoise(noiseProducer).addBackground(new GradiatedBackgroundProducer()).build();
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        //TODO: Put into redis
        HttpSession session = request.getSession(true);
        session.setAttribute(session.getId(), captcha.getAnswer());
    }

    @RequestMapping(value = "/captcha/{captcha}/verify", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto captchaVerify(@PathVariable String captcha) {
        boolean result = this.captchaVerifier.loginCaptchaVerify(captcha);
        BaseDto baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(result);
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/loginName/{loginName}/isexist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto loginNameIsExist(@PathVariable String loginName) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.loginNameIsExist(loginName));
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);

        return baseDto;
    }

}
