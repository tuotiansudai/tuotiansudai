package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.spring.security.CaptchaHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "图形验证码")
public class MobileAppImageCaptchaController extends MobileAppBaseController {

    @Autowired
    private CaptchaHelper captchaHelper;

    @RequestMapping(value = "/image-captcha", method = RequestMethod.POST)
    @ApiOperation("获取图片验证码")
    public BaseResponseDto<ImageCaptchaResponseDataDto> loginCaptcha(@RequestBody BaseParamDto baseParamDto) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight,
                this.captchaHelper.getCaptcha(baseParamDto.getBaseParam().getDeviceId()));

        captchaHelper.storeCaptcha(captcha.getAnswer(), baseParamDto.getBaseParam().getDeviceId());

        String imageCaptcha = captchaHelper.transferImageToBase64(captcha.getImage());

        BaseResponseDto<ImageCaptchaResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(new ImageCaptchaResponseDataDto(imageCaptcha));
        return baseResponseDto;
    }

    @RequestMapping(value = "/get/show-image-captcha", method = RequestMethod.POST)
    @ApiOperation("是否需要图形验证码")
    public BaseResponseDto isShowImageCaptcha(HttpServletRequest request) {
        boolean isNeedImageCaptcha = captchaHelper.isImageCaptchaNecessary(request.getRemoteAddr());
        if (isNeedImageCaptcha) {
            return new BaseResponseDto(ReturnMessage.NEED_IMAGE_CAPTCHA.getCode(),ReturnMessage.NEED_IMAGE_CAPTCHA.getMsg());
        }

        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(),ReturnMessage.SUCCESS.getMsg());
    }
}
