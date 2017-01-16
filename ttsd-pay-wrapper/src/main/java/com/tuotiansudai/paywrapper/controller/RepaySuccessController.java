package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/repay-success")
public class RepaySuccessController {

    @Autowired
    private CouponRepayService couponRepayService;

    @ResponseBody
    @RequestMapping(value = "/coupon-repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncInvestNotify(@RequestBody long loanRepayId) {
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(couponRepayService.repay(loanRepayId, true));
        return dto;
    }
}
