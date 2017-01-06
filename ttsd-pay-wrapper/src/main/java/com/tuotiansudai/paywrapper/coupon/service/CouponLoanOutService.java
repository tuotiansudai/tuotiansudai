package com.tuotiansudai.paywrapper.coupon.service;

import java.util.Map;

public interface CouponLoanOutService {

    boolean sendRedEnvelope(long loanId);

    String transferRedEnvelopCallBack(Map<String, String> paramsMap, String queryString);

    boolean sendRedEnvelopTransferInBalance(long userCouponId);
}
