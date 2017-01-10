package com.tuotiansudai.paywrapper.loanout;

import java.util.Map;

public interface CouponLoanOutService {

    boolean sendRedEnvelope(long loanId);

    String transferRedEnvelopNotify(Map<String, String> paramsMap, String queryString);

    boolean sendRedEnvelopTransferInBalanceCallBack(long userCouponId);
}
