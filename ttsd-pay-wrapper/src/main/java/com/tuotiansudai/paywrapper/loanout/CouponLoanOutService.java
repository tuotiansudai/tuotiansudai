package com.tuotiansudai.paywrapper.loanout;

import com.tuotiansudai.exception.AmountTransferException;

import java.util.Map;

public interface CouponLoanOutService {

    boolean sendRedEnvelope(long loanId);

    String transferRedEnvelopNotify(Map<String, String> paramsMap, String queryString);

    boolean sendRedEnvelopTransferInBalanceCallBack(long userCouponId);
}
