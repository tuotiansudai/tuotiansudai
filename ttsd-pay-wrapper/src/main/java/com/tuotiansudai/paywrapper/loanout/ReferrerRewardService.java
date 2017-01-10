package com.tuotiansudai.paywrapper.loanout;

import java.util.Map;

public interface ReferrerRewardService {

    boolean rewardReferrer(long loanId);

    boolean transferReferrerCallBack(long orderId);

    String transferReferrerRewardNotify(Map<String, String> paramsMap, String queryString);
}
