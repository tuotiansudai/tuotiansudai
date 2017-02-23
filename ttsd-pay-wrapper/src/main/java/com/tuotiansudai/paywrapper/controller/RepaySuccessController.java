package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.message.RepaySuccessMessage;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.loanout.CouponRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/repay-success")
public class RepaySuccessController {

    static Logger logger = Logger.getLogger(RepaySuccessController.class);

    @Autowired
    private CouponRepayService couponRepayService;
    @Autowired
    private ExtraRateService extraRateService;

    @ResponseBody
    @RequestMapping(value = "/coupon-repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> couponRepay(@RequestBody RepaySuccessMessage repaySuccessMessage) {
        boolean isSuccess = true;
        try {
            couponRepayService.repay(repaySuccessMessage.getLoanRepayId(), repaySuccessMessage.isAdvanced());
        } catch (Exception e) {
            isSuccess = false;
            logger.error("还款发放优惠券收益失败", e);
        }
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/extra-rate-repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> extraRateNormalRepay(@RequestBody RepaySuccessMessage repaySuccessMessage) {
        boolean isSuccess = true;
        try {
            if(repaySuccessMessage.isAdvanced()){
                logger.info(MessageFormat.format("extra_rate_advance_repay begin {0} ..",String.valueOf(repaySuccessMessage.getLoanRepayId())));
                extraRateService.advanceRepay(repaySuccessMessage.getLoanRepayId());
            }else{
                logger.info(MessageFormat.format("extra_rate_normal_repay begin {0} ..",String.valueOf(repaySuccessMessage.getLoanRepayId())));
                extraRateService.normalRepay(repaySuccessMessage.getLoanRepayId());
            }
        } catch (Exception e) {
            isSuccess = false;
            logger.error("还款发放阶梯加息收益失败", e);
        }
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/async_coupon_repay_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncCouponRepayNotify(@RequestBody long notifyRequestId) {
        return this.couponRepayService.asyncCouponRepayCallback(notifyRequestId);
    }

    @ResponseBody
    @RequestMapping(value = "/async_extra_rate_normal_repay_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncextraRateNormalRepayNotify(@RequestBody long notifyRequestId) {
        return this.extraRateService.asyncExtraRateInvestCallback(notifyRequestId);
    }

}
