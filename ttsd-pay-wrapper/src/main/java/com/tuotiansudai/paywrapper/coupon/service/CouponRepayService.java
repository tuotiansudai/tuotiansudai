package com.tuotiansudai.paywrapper.coupon.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CouponRepayNotifyRequestModel;

import java.util.Map;

public interface CouponRepayService {

    void repay(long loanRepayId, boolean isAdvanced);

    void generateCouponRepay(long loanId);

    String couponRepayCallback(Map<String, String> paramsMap, String queryString);

    BaseDto<PayDataDto> asyncCouponRepayCallback();

    void processOneCallback(CouponRepayNotifyRequestModel callbackRequestModel);

}
