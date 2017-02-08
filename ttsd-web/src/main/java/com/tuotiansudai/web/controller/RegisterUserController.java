package com.tuotiansudai.web.controller;


import com.google.common.base.Strings;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.PrepareUserService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.security.CaptchaHelper;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.RequestIPParser;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/register")
public class RegisterUserController {

    private final static Logger logger = Logger.getLogger(RegisterUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private PrepareUserService prepareService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerRedirect() {
        return new ModelAndView("redirect:/register/user");
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ModelAndView registerUser(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/register-user");
        modelAndView.addObject("referrer", userService.getMobile(request.getParameter("referrer")));
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/user/shared-prepare", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> prepareRegister(@Valid @ModelAttribute PrepareRegisterRequestDto requestDto, BindingResult bindingResult, HttpServletResponse response) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto;
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            logger.info("[APP SHARE IOS] :" + message);
            baseDataDto = new BaseDataDto(false, message);
            baseDto.setData(baseDataDto);
            return baseDto;
        }
        baseDataDto = prepareService.prepareRegister(requestDto);
        if (baseDataDto.getStatus()) {
            Cookie cookie = new Cookie("registerMobile", requestDto.getMobile());
            cookie.setPath("/activity/app-share");
            response.addCookie(cookie);
        }
        baseDto.setData(baseDataDto);
        return baseDto;
    }

    @RequestMapping(value = "/user/shared", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> register(@Valid @ModelAttribute RegisterUserDto requestDto, BindingResult bindingResult, HttpServletResponse response) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto;
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            logger.info("[APP SHARE ANDROID] :" + message);
            baseDataDto = new BaseDataDto(false, message);
            baseDto.setData(baseDataDto);
            return baseDto;
        }
        requestDto.setChannel("shareAB");
        baseDataDto = prepareService.register(requestDto);
        if (baseDataDto.getStatus()) {
            Cookie cookie = new Cookie("registerMobile", requestDto.getMobile());
            cookie.setPath("/activity/app-share");
            response.addCookie(cookie);
        }
        baseDto.setData(baseDataDto);
        return baseDto;
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerUser(@Valid @ModelAttribute RegisterUserDto registerUserDto, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        boolean isRegisterSuccess;
        if (request.getSession().getAttribute("channel") != null) {
            registerUserDto.setChannel(String.valueOf(request.getSession().getAttribute("channel")));
        }
        logger.info(MessageFormat.format("[Register User {0}] controller starting...", registerUserDto.getMobile()));
        isRegisterSuccess = this.userService.registerUser(registerUserDto);
        logger.info(MessageFormat.format("[Register User {0}] controller invoked service ({0})", registerUserDto.getMobile(), String.valueOf(isRegisterSuccess)));


        if (!isRegisterSuccess) {
            redirectAttributes.addFlashAttribute("originalFormData", registerUserDto);
            redirectAttributes.addFlashAttribute("success", false);
        }

        if (isRegisterSuccess) {
            logger.info(MessageFormat.format("[Register User {0}] authenticate starting...", registerUserDto.getMobile()));
            myAuthenticationUtil.createAuthentication(registerUserDto.getMobile(), Source.WEB);
            logger.info(MessageFormat.format("[Register User {0}] authenticate completed", registerUserDto.getMobile()));
        }

        String successUrl = Strings.isNullOrEmpty(registerUserDto.getRedirectToAfterRegisterSuccess()) ? "/" : registerUserDto.getRedirectToAfterRegisterSuccess();
        String url = MessageFormat.format("redirect:{0}", isRegisterSuccess ? successUrl : "/register/user");
        logger.info(MessageFormat.format("[Register User {0}] controller redirect to {1}", registerUserDto.getMobile(), url));
        return new ModelAndView(url);
    }

    @RequestMapping(value = "/user/mobile/{mobile:^\\d{11}$}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> mobileIsExist(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.mobileIsExist(mobile));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;

    }

    @RequestMapping(value = "/user/mobile/{mobile:^\\d{11}$}/is-register", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> mobileIsRegister(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.mobileIsRegister(mobile));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/user/login-name/{loginName}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> loginNameIsExist(@PathVariable String loginName) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.loginNameIsExist(loginName.toLowerCase()));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/user/referrer/{loginNameOrMobile}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> loginNameOrMobileIsExist(@PathVariable String loginNameOrMobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.loginNameOrMobileIsExist(loginNameOrMobile.toLowerCase()));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(path = "/user/send-register-captcha", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<SmsDataDto> sendRegisterCaptcha(HttpServletRequest httpServletRequest,
                                                   @Valid @ModelAttribute RegisterCaptchaDto dto) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        HttpSession session = httpServletRequest.getSession(false);
        boolean result = this.captchaHelper.captchaVerify(dto.getImageCaptcha(), session != null ? session.getId() : "", httpServletRequest.getRemoteAddr());
        if (result) {
            return smsCaptchaService.sendRegisterCaptcha(dto.getMobile(), RequestIPParser.parse(httpServletRequest));
        }
        return baseDto;
    }

    @RequestMapping(path = "/user/{mobile:^\\d{11}$}/send-register-captcha", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<SmsDataDto> sendRegisterCaptcha(HttpServletRequest httpServletRequest, @PathVariable String mobile) {

        return smsCaptchaService.sendRegisterCaptcha(mobile, RequestIPParser.parse(httpServletRequest));
    }

    @RequestMapping(value = "/user/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.REGISTER_CAPTCHA));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/user/image-captcha", method = RequestMethod.GET)
    public void registerImageCaptcha(HttpServletRequest request, HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 38;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        captchaHelper.storeCaptcha(captcha.getAnswer(), request.getSession(false) != null ? request.getSession(false).getId() : null);
    }
}
