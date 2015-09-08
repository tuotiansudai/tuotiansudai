package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.service.RetrievePasswordService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.CaptchaGenerator;
import com.tuotiansudai.utils.CaptchaVerifier;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
        return new ModelAndView("/input-retrieve-password-info");
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
    public void retrievePasswordCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        redisWrapperClient.setex(session.getId(), 30, captcha.getAnswer());
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
        BaseDto baseDto = new BaseDto();
        BaseDataDto baseDataDto = new BaseDataDto();
        if (smsCaptchaService.verifyMobileCaptcha(mobile, captcha)){
            baseDataDto.setStatus(true);
            baseDto.setData(baseDataDto);
            return baseDto;
        }
        baseDataDto.setStatus(false);
        baseDto.setData(baseDataDto);
        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/{password}/retrieve-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> registerAccount(@PathVariable String mobile, @PathVariable String captcha,@PathVariable String password) {
        BaseDto baseDto = new BaseDto();
        BaseDataDto baseDataDto = new BaseDataDto();
        if (retrievePasswordService.mobileRetrievePassword(mobile,captcha,password)){
            baseDataDto.setStatus(true);
            baseDto.setData(baseDataDto);
            return baseDto;
        }
        baseDataDto.setStatus(false);
        baseDto.setData(baseDataDto);
        return baseDto;
    }
}
