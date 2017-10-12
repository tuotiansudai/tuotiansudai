package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuRepayDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.HuiZuRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/repay")
public class HuizuRepayController {
    static Logger logger = Logger.getLogger(HuizuRepayController.class);
    @Autowired
    private HuiZuRepayService huiZuRepayService;

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> repayPassword(@Valid @RequestBody HuiZuRepayDto repayDto) {

        return huiZuRepayService.passwordRepay(repayDto);
    }

    @RequestMapping(value = "/no-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> repayNoPassword(@Valid @RequestBody HuiZuRepayDto repayDto) {

        return huiZuRepayService.noPasswordRepay(repayDto);
    }
}
