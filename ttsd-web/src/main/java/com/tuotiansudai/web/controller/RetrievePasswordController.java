package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RetrievePasswordDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.service.RetrievePasswordService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.security.CaptchaHelper;
import com.tuotiansudai.util.RequestIPParser;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/mobile-retrieve-password")
public class RetrievePasswordController {
    @Autowired
    private UserService userService;
    @Autowired
    private SmsCaptchaService smsCaptchaService;
    @Autowired
    private CaptchaHelper captchaHelper;
    @Autowired
    private RetrievePasswordService retrievePasswordService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView inputCellphone() {
        return new ModelAndView("/retrieve", "responsive", true);
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/new-password-page", method = RequestMethod.GET)
    public ModelAndView inputPassword(@PathVariable String mobile, @PathVariable String captcha) {
        if (smsCaptchaService.verifyMobileCaptcha(mobile, captcha, SmsCaptchaType.RETRIEVE_PASSWORD_CAPTCHA)) {
            return new ModelAndView("/input-password").addObject("mobile", mobile).addObject("captcha", captcha);
        }
        return new ModelAndView("redirect:/mobile-retrieve-password");
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/is-exist", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> mobileIsExist(@PathVariable String mobile) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(userService.mobileIsExist(mobile));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/image-captcha", method = RequestMethod.GET)
    public void imageCaptcha(@RequestParam(value = "flush", defaultValue = "true", required = false) boolean flush,
                             HttpServletRequest request, HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = this.captchaHelper.getCaptcha(request.getSession().getId(), captchaHeight, captchaWidth, flush);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        if (smsCaptchaService.verifyMobileCaptcha(mobile, captcha, SmsCaptchaType.RETRIEVE_PASSWORD_CAPTCHA)) {
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

    @RequestMapping(value = "/m", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> mobileRetrievePasswordForMobileSite(@Valid @ModelAttribute RetrievePasswordDto retrievePasswordDto) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        try {
            boolean success = retrievePasswordService.mobileRetrievePassword(retrievePasswordDto);
            dataDto.setStatus(success);
            dataDto.setMessage(success?"OK":"短信验证码不正确");
        } catch (Exception e) {
            dataDto.setStatus(false);
            dataDto.setMessage(e.getMessage());
        }
        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/imageCaptcha/{imageCaptcha:^[a-zA-Z0-9]{5}$}/send-mobile-captcha/{isVoice}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<SmsDataDto> mobileCaptcha(HttpServletRequest httpServletRequest, @PathVariable String mobile, @PathVariable String imageCaptcha, @PathVariable boolean isVoice) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        boolean result = captchaHelper.captchaVerify(imageCaptcha, httpServletRequest.getSession(false).getId(), httpServletRequest.getRemoteAddr());
        if (result) {
            return smsCaptchaService.sendRetrievePasswordCaptcha(mobile, isVoice, RequestIPParser.parse(httpServletRequest));
        }
        return baseDto;
    }

}
