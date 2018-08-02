package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.luxury.LuxuryStageRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path = "/luxury")
public class LuxuryStageRepayController {

    private static Logger logger = Logger.getLogger(LuxuryStageRepayController.class);

    private final LuxuryStageRepayService luxuryStageRepayService;

    @Autowired
    public LuxuryStageRepayController(LuxuryStageRepayService luxuryStageRepayService) {
        this.luxuryStageRepayService = luxuryStageRepayService;
    }

    @RequestMapping(value = "/{luxuryOrderId}/period/{period}/mobile/{mobile}/amount/{amount}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> repay(@PathVariable long luxuryOrderId,
                                         @PathVariable int period,
                                         @PathVariable String mobile,
                                         @PathVariable long amount) {
        return luxuryStageRepayService.repay(luxuryOrderId, period, mobile, amount);
    }

    @RequestMapping(value = "/no-password-repay/{luxuryOrderId}/period/{period}/mobile/{mobile}/amount/{amount}/auto-repay/{autoRepay}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> creditLoanRepayNoPwd(@PathVariable long luxuryOrderId,
                                                    @PathVariable int period,
                                                    @PathVariable String mobile,
                                                    @PathVariable long amount,
                                                    @PathVariable boolean autoRepay) {
        return luxuryStageRepayService.noPasswordRepay(luxuryOrderId, period, mobile, amount, autoRepay);
    }
}
