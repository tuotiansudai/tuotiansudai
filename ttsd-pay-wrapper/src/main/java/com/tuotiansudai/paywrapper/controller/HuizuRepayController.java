package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuRepayDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.HuiZuRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/repay")
public class HuizuRepayController {
    static Logger logger = Logger.getLogger(HuizuRepayController.class);
    @Autowired
    private HuiZuRepayService huiZuRepayService;

    @RequestMapping(value = "/password")
    @ResponseBody
    public BaseDto<PayFormDataDto> repayPassword(HuiZuRepayDto repayDto) {
        return huiZuRepayService.passwordRepay(repayDto);
    }
}
