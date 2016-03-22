package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.service.AgreementService;
import com.tuotiansudai.service.NoPasswordInvestService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import com.tuotiansudai.util.RequestIPParser;
import com.tuotiansudai.web.util.LoginUserInfo;
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
@RequestMapping(path = "/no-password-invest")
public class NoPasswordInvestController {
    @Autowired
    private NoPasswordInvestService noPasswordInvestService;
    @Autowired
    private AgreementService agreementService;
    @Autowired
    private CaptchaHelper captchaHelper;
    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @ResponseBody
    @RequestMapping(path = "/enabled", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> enabledNoPasswordInvest() {
        String loginName = LoginUserInfo.getLoginName();
        noPasswordInvestService.enabledNoPasswordInvest(loginName);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        baseDto.setData(dataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }

    @ResponseBody
    @RequestMapping(path = "/disabled", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> disabledNoPasswordInvest() {
        String loginName = LoginUserInfo.getLoginName();
        noPasswordInvestService.disabledNoPasswordInvest(loginName);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        baseDto.setData(dataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView agreement(@Valid @ModelAttribute AgreementDto agreementDto) {
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(LoginUserInfo.getLoginName(), agreementDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }

    @RequestMapping(value = "/image-captcha", method = RequestMethod.GET)
    public void registerImageCaptcha(HttpServletResponse response) {
        int captchaWidth = 70;
        int captchaHeight = 38;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        captchaHelper.storeCaptcha(CaptchaHelper.TURN_OFF_NO_PASSWORD_INVEST, captcha.getAnswer());
    }

    @RequestMapping(path = "/send-no-password-invest-captcha", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(HttpServletRequest httpServletRequest, @Valid @ModelAttribute RegisterCaptchaDto dto) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        boolean result = this.captchaHelper.captchaVerify(CaptchaHelper.TURN_OFF_NO_PASSWORD_INVEST, dto.getImageCaptcha());
        if (result) {
            return smsCaptchaService.sendNoPasswordInvestCaptcha(dto.getMobile(), RequestIPParser.parse(httpServletRequest));
        }
        return baseDto;
    }

    @RequestMapping(value = "/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.TURN_OFF_NO_PASSWORD_INVEST));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }


    @ResponseBody
    @RequestMapping(value = "/writeRemindFlag", method = RequestMethod.GET)
    public String writeRemindFlagInRedis() {
        String loginName = LoginUserInfo.getLoginName();
        noPasswordInvestService.writeRemindFlag(loginName);
        return "";
    }
}
