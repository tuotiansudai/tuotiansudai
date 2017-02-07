package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.service.ExperienceRepayService;
import com.tuotiansudai.paywrapper.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/experience")
public class ExperienceRepayController {

    @Autowired
    private ExperienceRepayService experienceRepayService;

    @ResponseBody
    @RequestMapping(value = "/repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> repay(@RequestBody long investId) {
        boolean isSuccess = experienceRepayService.repay(investId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/async_experience_interest_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncExperienceInterestNotify(@RequestBody long notifyRequestId){
        return this.experienceRepayService.asyncExperienceInterestNotify(notifyRequestId);
    }
}
