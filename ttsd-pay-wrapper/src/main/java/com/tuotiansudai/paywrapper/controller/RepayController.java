package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.paywrapper.service.RepayService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class RepayController {

    static Logger logger = Logger.getLogger(RepayController.class);

    @Resource(name = "normalRepayServiceImpl")
    private RepayService normalRepayService;

    @Resource(name = "advanceRepayServiceImpl")
    private RepayService advanceRepayService;

    @RequestMapping(value = "/repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> repay(@Valid @RequestBody RepayDto dto) {
        return dto.isAdvanced() ? advanceRepayService.repay(dto.getLoanId()) : normalRepayService.repay(dto.getLoanId());
    }

    @RequestMapping(value = "/auto-repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> autoRepay(@RequestBody long loanId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        try {
            payDataDto.setStatus(normalRepayService.autoRepay(loanId));
        } catch (Exception e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error("auto repay failed", e);
        }
        return baseDto;
    }
}
