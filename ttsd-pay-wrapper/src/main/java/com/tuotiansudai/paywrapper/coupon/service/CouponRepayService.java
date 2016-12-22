package com.tuotiansudai.paywrapper.coupon.service;

import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CouponRepayNotifyRequestModel;
import com.tuotiansudai.repository.model.LoanRepayModel;

import java.util.Map;

public interface CouponRepayService {

    void sendCouponRepayAmount(long loanRepayId, String payUserId, CouponRepayModel couponRepayModel, LoanRepayModel loanRepayModel, UserCouponModel userCouponModel, long transferAmount) throws Exception;

    void generateCouponRepay(long loanId);

    String couponRepayCallback(Map<String, String> paramsMap, String queryString);

    BaseDto<PayDataDto> asyncCouponRepayCallback(long notifyRequestId);

    void processOneCallback(CouponRepayNotifyRequestModel callbackRequestModel);

}