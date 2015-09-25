package com.tuotiansudai.web.controller;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterCaptchaDto;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.CaptchaGenerator;
import com.tuotiansudai.utils.CaptchaVerifier;
import com.tuotiansudai.utils.RequestIPParser;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(path = "/register/user")
public class RegisterUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private CaptchaVerifier captchaVerifier;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerUser() {
        return new ModelAndView("/register-user");
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerUser(@Valid @ModelAttribute RegisterUserDto registerUserDto, RedirectAttributes redirectAttributes) {
        boolean isRegisterSuccess = this.userService.registerUser(registerUserDto);
        if (!isRegisterSuccess) {
            redirectAttributes.addFlashAttribute("originalFormData", registerUserDto);
            redirectAttributes.addFlashAttribute("success", isRegisterSuccess);
        }

        return new ModelAndView(isRegisterSuccess ? "redirect:/register/account" : "redirect:/register/user");
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> mobileIsExist(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.mobileIsExist(mobile));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;

    }

    @RequestMapping(value = "/login-name/{loginName}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> loginNameIsExist(@PathVariable String loginName) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.loginNameIsExist(loginName.toLowerCase()));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(path = "/send-register-captcha", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto sendRegisterCaptcha(HttpServletRequest httpServletRequest, @RequestBody RegisterCaptchaDto dto) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        boolean result = this.captchaVerifier.registerImageCaptchaVerify(dto.getImageCaptcha());
        if (result) {
            dataDto.setStatus(smsCaptchaService.sendRegisterCaptcha(dto.getMobile(), RequestIPParser.getRequestIp(httpServletRequest)));
        }
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

    @RequestMapping(value = "/image-captcha", method = RequestMethod.GET)
    public void registerImageCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        int captchaWidth = 70;
        int captchaHeight = 38;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        redisWrapperClient.setex(session.getId(), 60, captcha.getAnswer());
    }

    @RequestMapping(value = "/image-captcha/{imageCaptcha:^[a-zA-Z0-9]{5}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto imageCaptchaVerify(@PathVariable String imageCaptcha) {
        boolean result = this.captchaVerifier.registerImageCaptchaVerify(imageCaptcha);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(result);
        baseDto.setData(dataDto);

        return baseDto;
    }
}
