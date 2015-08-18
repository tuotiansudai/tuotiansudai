package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BaseController {

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
    public BaseDto<BaseDataDto> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(this.userService.registerUser(registerUserDto));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> registerAccount(@Valid @RequestBody RegisterAccountDto registerAccountDto) {
        return this.userService.registerAccount(registerAccountDto);
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/isexist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> mobileIsExist(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.mobileIsExist(mobile));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;

    }

    @RequestMapping(value = "/loginName/{loginName}/isexist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> loginNameIsExist(@PathVariable String loginName) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.loginNameIsExist(loginName));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/sendregistercaptcha", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> sendRegisterCaptcha(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.sendRegisterCaptcha(mobile));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyRegisterCaptcha(mobile, captcha));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;

    }

}
