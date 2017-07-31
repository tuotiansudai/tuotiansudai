package com.tuotiansudai.api.controller.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.dto.v2_0.ImageCaptchaResponseDataDto;
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

@RestController
@Api(description = "V2.0图片验证码")
public class MobileAppImageCaptchaV2Controller extends MobileAppBaseController {

    @Autowired
    private CaptchaHelper captchaHelper;

    @RequestMapping(value = "/image-captcha", method = RequestMethod.POST)
    @ApiOperation("图片验证码")
    public BaseResponseDto<ImageCaptchaResponseDataDto> getImageCaptcha(@RequestBody BaseParamDto baseParamDto) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = this.captchaHelper.getCaptcha(baseParamDto.getBaseParam().getDeviceId(), captchaHeight, captchaWidth, true);
        String imageCaptcha = captchaHelper.transferImageToBase64(captcha.getImage());
        BaseResponseDto<ImageCaptchaResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(new ImageCaptchaResponseDataDto(imageCaptcha));
        return baseResponseDto;
    }
}
