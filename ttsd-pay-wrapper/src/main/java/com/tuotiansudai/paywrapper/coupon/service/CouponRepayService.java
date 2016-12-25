package com.tuotiansudai.paywrapper.coupon.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CouponRepayDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CouponRepayNotifyRequestModel;

import java.util.Map;

public interface CouponRepayService {

    void sendCouponRepayAmount(CouponRepayDto couponRepayDto) throws Exception;

    void generateCouponRepay(long loanId);

    String couponRepayCallback(Map<String, String> paramsMap, String queryString);

    BaseDto<PayDataDto> asyncCouponRepayCallback(long notifyRequestId);

    void processOneCallback(CouponRepayNotifyRequestModel callbackRequestModel);

}