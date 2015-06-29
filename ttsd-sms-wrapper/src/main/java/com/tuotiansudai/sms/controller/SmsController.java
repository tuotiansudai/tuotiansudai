package com.tuotiansudai.sms.controller;

import com.tuotiansudai.sms.dto.ResultDataDto;
import com.tuotiansudai.sms.dto.ResultDto;
import com.tuotiansudai.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/mobile/{mobile}/captcha/{captcha}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto sendRegisterCaptcha(@PathVariable("mobile") String mobile,
                                         @PathVariable("captcha") String captcha) {

        boolean sendSuccess = smsService.sendRegisterCaptcha(mobile, captcha);

        ResultDto resultDto = new ResultDto();
        ResultDataDto data = new ResultDataDto();
        data.setStatus(sendSuccess);
        resultDto.setSuccess(true);
        resultDto.setData(data);
        return resultDto;
    }
}
