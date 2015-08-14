package com.tuotiansudai.web.controller;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.security.CaptchaVerifier;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.CaptchaGenerator;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^[a-zA-Z0-9]{5}$}/sendregistercaptcha", method = RequestMethod.GET)
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

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void registerCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        redisWrapperClient.setex(session.getId(), 30, captcha.getAnswer());
    }

    @RequestMapping(value = "/captcha/{captcha:^[a-zA-Z0-9]{5}$}/verify", method = RequestMethod.GET)
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
