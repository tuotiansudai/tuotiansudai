package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.InvestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class InvestController {

    static Logger logger = Logger.getLogger(InvestController.class);

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> invest(@Valid @RequestBody InvestDto dto) {
        return investService.invest(dto);
    }

    @RequestMapping(value = "/auto-invest", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> autoInvest(@RequestBody long loanId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        try {
            investService.autoInvest(loanId);
            payDataDto.setStatus(true);
        } catch (Exception e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error("auto invest failed", e);
        }
        return baseDto;
    }
}
