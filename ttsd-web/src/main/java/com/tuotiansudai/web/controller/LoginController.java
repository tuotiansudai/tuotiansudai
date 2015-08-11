package com.tuotiansudai.web.controller;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.renderer.DefaultWordRenderer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);

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
        request.getSession().setAttribute(request.getSession().getId(), captcha.getAnswer());
    }

    @RequestMapping(value = "/captcha/{captcha:^[a-zA-Z0-9]{5}$}/verify", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto captchaVerify(HttpServletRequest request, @PathVariable String captcha) {
        String jSessionId = this.getJSessionId(request);

        Object existingCaptcha = request.getSession().getAttribute(jSessionId);

        BaseDto baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(existingCaptcha != null && captcha.equalsIgnoreCase((String) existingCaptcha));
        baseDto.setData(dataDto);

        return baseDto;
    }

    private String getJSessionId(HttpServletRequest request) {
        List<Cookie> cookies = Arrays.asList(request.getCookies());
        Optional<Cookie> cookieOptional = Iterators.tryFind(cookies.iterator(), new Predicate<Cookie>() {
            @Override
            public boolean apply(Cookie cookie) {
                return cookie.getName().equalsIgnoreCase("jsessionid");
            }
        });

        if (cookieOptional.isPresent()) {
            return cookieOptional.get().getValue();
        }

        return null;
    }
}
