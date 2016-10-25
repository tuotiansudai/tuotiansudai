package com.tuotiansudai.paywrapper.coupon.service;

import java.util.Map;

public interface CouponRepayService {

    void repay(long loanRepayId, boolean isAdvanced);

    void generateCouponRepay(long loanId);

    String couponRepayCallback(Map<String, String> paramsMap, String queryString);

}
