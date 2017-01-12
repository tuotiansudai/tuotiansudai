package com.tuotiansudai.paywrapper.loanout;

import com.tuotiansudai.exception.AmountTransferException;

import java.util.Map;

public interface ReferrerRewardService {

    boolean rewardReferrer(long loanId);

    boolean transferReferrerCallBack(long orderId) throws AmountTransferException;

    String transferReferrerRewardNotify(Map<String, String> paramsMap, String queryString);
}
