package com.tuotiansudai.web.controller;


import com.tuotiansudai.web.dto.ResultDataDto;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.web.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SmsCaptchaController {

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @RequestMapping(value = "/register/mobile/{mobile}/sendSms", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto sendRegisterByMobileNumberSMS(@PathVariable String mobile) {
        ResultDto resultDto = new ResultDto();
        ResultDataDto data = new ResultDataDto();
        try {
            data.setStatus(smsCaptchaService.sendSmsbyMobileNumberRegister(mobile));
        } catch (Exception e) {
            data.setStatus(false);
            e.printStackTrace();
        }
        resultDto.setSuccess(true);
        resultDto.setData(data);
        return resultDto;
    }

    @RequestMapping(value = "/register/mobile/{mobile}/captcha/{captcha}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        ResultDto resultDto = new ResultDto();

        ResultDataDto data = new ResultDataDto();

        data.setStatus(smsCaptchaService.verifyCaptcha(mobile, captcha));

        resultDto.setSuccess(true);

        resultDto.setData(data);

        return resultDto;

    }

}
