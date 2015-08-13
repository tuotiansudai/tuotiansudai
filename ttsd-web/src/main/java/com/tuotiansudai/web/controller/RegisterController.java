package com.tuotiansudai.web.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.security.CaptchaVerifier;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.renderer.DefaultWordRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private CaptchaVerifier captchaVerifier;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/register");
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(this.userService.registerUser(registerUserDto));
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto registerAccount(@Valid @RequestBody RegisterAccountDto registerAccountDto) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(this.userService.registerAccount(registerAccountDto));
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/isexist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto mobileIsExist(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.mobileIsExist(mobile));
        BaseDto baseDto = new BaseDto();
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

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/{captcha}/sendregistercaptcha", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto sendRegisterCaptcha(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        boolean result = this.captchaVerifier.registerPhotoCaptchaVerify(captcha);
        if (result) {
            dataDto.setStatus(smsCaptchaService.sendRegisterCaptcha(mobile));
        } else {
            dataDto.setStatus(result);
        }
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyRegisterCaptcha(mobile, captcha));
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);

        return baseDto;

    }

    @RequestMapping(value = "/photo/captcha", method = RequestMethod.GET)
    public void registerCaptcha(HttpServletRequest request, HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        HttpSession session = request.getSession(true);
        Captcha.Builder captchaBuilder = new Captcha.Builder(captchaWidth, captchaHeight);
        DefaultWordRenderer wordRenderer = new DefaultWordRenderer(Lists.newArrayList(Color.BLACK), Lists.newArrayList(new Font("Geneva", Font.BOLD, 24)));
        CurvedLineNoiseProducer noiseProducer = new CurvedLineNoiseProducer(Color.BLACK, 1.0f);
        Captcha captcha = captchaBuilder.addText(wordRenderer).addNoise(noiseProducer).addBackground(new GradiatedBackgroundProducer()).build();
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        redisWrapperClient.setex(session.getId(), 30, captcha.getAnswer());
    }

    @RequestMapping(value = "/photo/captcha/{captcha}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto photoCaptchaVerify(@PathVariable String captcha) {
        boolean result = this.captchaVerifier.registerPhotoCaptchaVerify(captcha);
        BaseDto baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(result);
        baseDto.setData(dataDto);

        return baseDto;
    }

}
