package com.tuotiansudai.web.controller;


import com.google.common.base.Strings;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.MyAuthenticationManager;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import com.tuotiansudai.util.RequestIPParser;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/register/user")
public class RegisterUserController {

    private final static Logger logger = Logger.getLogger(RegisterUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerUser(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/register-user");
        modelAndView.addObject("referrer", userService.getMobile(request.getParameter("referrer")));

        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerUser(@Valid @ModelAttribute RegisterUserDto registerUserDto, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        boolean isRegisterSuccess = false;
        try {
            if (request.getSession().getAttribute("channel") != null) {
                registerUserDto.setChannel(String.valueOf(request.getSession().getAttribute("channel")));
            }
            logger.info(MessageFormat.format("[Register User {0}] controller starting...", registerUserDto.getMobile()));
            isRegisterSuccess = this.userService.registerUser(registerUserDto);
            logger.info(MessageFormat.format("[Register User {0}] controller invoked service ({0})", registerUserDto.getMobile(), String.valueOf(isRegisterSuccess)));
        } catch (ReferrerRelationException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (!isRegisterSuccess) {
            redirectAttributes.addFlashAttribute("originalFormData", registerUserDto);
            redirectAttributes.addFlashAttribute("success", false);
        }

        if (isRegisterSuccess) {
            logger.info(MessageFormat.format("[Register User {0}] authenticate starting...", registerUserDto.getMobile()));
            myAuthenticationManager.createAuthentication(registerUserDto.getMobile());
            logger.info(MessageFormat.format("[Register User {0}] authenticate completed", registerUserDto.getMobile()));
        }

        String successUrl = Strings.isNullOrEmpty(registerUserDto.getRedirectToAfterRegisterSuccess()) ? "/" : registerUserDto.getRedirectToAfterRegisterSuccess();
        String url = MessageFormat.format("redirect:{0}", isRegisterSuccess ? successUrl : "/register/user");
        logger.info(MessageFormat.format("[Register User {0}] controller redirect to {1}", registerUserDto.getMobile(), url));
        return new ModelAndView(url);
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

    @RequestMapping(value = "/referrer/{loginNameOrMobile}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> loginNameOrMobileIsExist(@PathVariable String loginNameOrMobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.loginNameOrMobileIsExist(loginNameOrMobile.toLowerCase()));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(path = "/send-register-captcha", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<SmsDataDto> sendRegisterCaptcha(HttpServletRequest httpServletRequest, @Valid @ModelAttribute RegisterCaptchaDto dto) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        boolean result = this.captchaHelper.captchaVerify(CaptchaHelper.REGISTER_CAPTCHA, dto.getImageCaptcha());
        if (result) {
            return smsCaptchaService.sendRegisterCaptcha(dto.getMobile(), RequestIPParser.parse(httpServletRequest));
        }
        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.REGISTER_CAPTCHA));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/image-captcha", method = RequestMethod.GET)
    public void registerImageCaptcha(HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 38;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        captchaHelper.storeCaptcha(CaptchaHelper.REGISTER_CAPTCHA, captcha.getAnswer());
    }
}
