package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.loanout.CouponRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/normal-repay")
public class NormalRepaySuccessController {
    static Logger logger = Logger.getLogger(NormalRepaySuccessController.class);
    @Autowired
    private CouponRepayService couponRepayService;

    @ResponseBody
    @RequestMapping(value = "/coupon-repay", method = RequestMethod.POST)
    public BaseDto<PayDataDto> sendRewardReferrer(@RequestBody long loanRepayId) {
        boolean isSuccess = true;
        try {
            couponRepayService.repay(loanRepayId,false);
        }catch (Exception e){
            isSuccess = false;
            logger.error("正常还款发放优惠券收益失败",e);
        }
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

}
