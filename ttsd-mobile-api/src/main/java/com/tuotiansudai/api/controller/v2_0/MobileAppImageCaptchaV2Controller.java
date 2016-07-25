package com.tuotiansudai.api.controller.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.dto.v2_0.ImageCaptchaResponseDataDto;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppImageCaptchaV2Controller extends MobileAppBaseController {

    @Autowired
    private CaptchaHelper captchaHelper;

    @RequestMapping(value = "/image-captcha", method = RequestMethod.POST)
    public BaseResponseDto getImageCaptcha(@RequestBody BaseParamDto baseParamDto) {
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
}
