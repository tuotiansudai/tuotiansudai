package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RetrievePasswordDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.RetrievePasswordService;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/mobile-retrieve-password")
public class RetrievePasswordController {
    @Autowired
    private UserService userService;
    @Autowired
    private SmsCaptchaService smsCaptchaService;
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private CaptchaVerifier captchaVerifier;
    @Autowired
    private RetrievePasswordService retrievePasswordService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView inputCellphone() {
        return new ModelAndView("/retrieve").addObject("mobile", "");
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/new-password-page", method = RequestMethod.GET)
    public ModelAndView inputPassword(@PathVariable String mobile, @PathVariable String captcha) {
        if (smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA)) {
            return new ModelAndView("/input-password").addObject("mobile", mobile).addObject("captcha", captcha);
        }
        return new ModelAndView("redirect:/mobile-retrieve-password");
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

    @RequestMapping(value = "/image-captcha", method = RequestMethod.GET)
    public void imageCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        redisWrapperClient.setex(session.getId(), 30, captcha.getAnswer());
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{imageCaptcha:^[a-zA-Z0-9]{5}$}/send-mobile-captcha", method = RequestMethod.GET)
    @ResponseBody
    //TODO get -> post
    public BaseDto<SmsDataDto> mobileCaptcha(HttpServletRequest httpServletRequest, @PathVariable String mobile, @PathVariable String imageCaptcha) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        boolean result = captchaVerifier.mobileRetrievePasswordImageCaptchaVerify(imageCaptcha);
        if (result) {
            return smsCaptchaService.sendRetrievePasswordCaptcha(mobile, RequestIPParser.getRequestIp(httpServletRequest));
        }
        return baseDto;
    }

    @RequestMapping(value = "/image-captcha/{imageCaptcha:^[a-zA-Z0-9]{5}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto imageCaptchaVerify(@PathVariable String imageCaptcha) {
        boolean result = this.captchaVerifier.mobileRetrievePasswordImageCaptchaVerify(imageCaptcha);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(result);
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        if (smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.RETRIEVE_PASSWORD_CAPTCHA)) {
            baseDataDto.setStatus(true);
        }
        return baseDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView mobileRetrievePassword(@Valid @ModelAttribute RetrievePasswordDto retrievePasswordDto) {
        if (retrievePasswordService.mobileRetrievePassword(retrievePasswordDto)) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("/input-password").addObject("mobile", retrievePasswordDto.getMobile()).addObject("captcha", retrievePasswordDto.getCaptcha());
    }
}
