package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.service.AdvanceTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/repay-success")
public class RepaySuccessController {

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private ExtraRateService extraRateService;

    @Autowired
    private AdvanceTransferService advanceTransferService;

    @ResponseBody
    @RequestMapping(value = "/coupon-repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncInvestRepay(@Valid @RequestBody RepayDto repayDto) {
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(couponRepayService.repay(repayDto.getLoanRepayId(), repayDto.isAdvanced()));
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/extra-repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncExtraRepay(@Valid @RequestBody RepayDto repayDto) {
        boolean result = repayDto.isAdvanced() ? extraRateService.advanceRepay(repayDto.getLoanRepayId()) : extraRateService.normalRepay(repayDto.getLoanRepayId());
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(result);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/advance-transfer", method = RequestMethod.POST)
    public BaseDto<PayDataDto> advanceTransfer(@RequestBody long loanRepayId) {
        boolean result = advanceTransferService.modifyTransfer(loanRepayId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(result);
        return dto;
    }

}
