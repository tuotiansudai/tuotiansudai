package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppImageCaptchaController extends MobileAppBaseController {


    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private HttpServletRequest httpServletRequest;


    @RequestMapping(value = "/image-captcha", method = RequestMethod.POST)
    public BaseResponseDto loginCaptcha(@RequestBody BaseParamDto baseParamDto) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);

        captchaHelper.storeCaptcha(CaptchaHelper.BASIC_CAPTCHA, captcha.getAnswer(), baseParamDto.getBaseParam().getDeviceId());

        String imageCaptcha = captchaHelper.transferImageToBase64(captcha.getImage());

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(new ImageCaptchaResponseDataDto(imageCaptcha));
        return baseResponseDto;
    }

    @RequestMapping(value = "/get/show-image-captcha", method = RequestMethod.POST)
    public BaseResponseDto isShowImageCaptcha(@RequestBody ImageCaptchaRequestDto requestDto) {
        boolean isNeedImageCaptcha = captchaHelper.checkImageCaptcha(requestDto.getType(), httpServletRequest.getRemoteAddr());
        if (isNeedImageCaptcha) {
            return new BaseResponseDto(ReturnMessage.NEED_IMAGE_CAPTCHA.getCode(),ReturnMessage.NEED_IMAGE_CAPTCHA.getMsg());
        }

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(),ReturnMessage.SUCCESS.getMsg());
    }
}
