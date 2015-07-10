package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

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

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/isExist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto mobileIsExist(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.mobileIsExist(mobile));
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);

        return baseDto;

    }

    @RequestMapping(value = "/loginName/{loginName}/isExist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto loginNameIsExist(@PathVariable String loginName) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.loginNameIsExist(loginName));
        BaseDto baseDto = new BaseDto();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/sendRegisterCaptcha", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto sendRegisterCaptcha(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.sendRegisterCaptcha(mobile));
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

}
