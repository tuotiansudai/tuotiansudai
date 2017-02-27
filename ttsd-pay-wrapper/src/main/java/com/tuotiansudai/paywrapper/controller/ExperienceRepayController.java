package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.service.ExperienceRepayService;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.impl.ExperienceRepayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/experience")
public class ExperienceRepayController {

    private static Logger logger = LoggerFactory.getLogger(ExperienceRepayController.class);

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
    @RequestMapping(value = "/post-repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> postCallback(@RequestBody long investRepayId){
        try {
            return experienceRepayService.postCallback(investRepayId);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);

        }

        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(false);
        return new BaseDto<>(baseDataDto);
    }
}
