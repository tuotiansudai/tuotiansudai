package com.tuotiansudai.paywrapper.service;

import java.util.Map;

public interface ReferrerRewardService {

    boolean rewardReferrer(long loanId);

    boolean transferReferrerReward(long orderId);

    String transferReferrerRewardCallBack(Map<String, String> paramsMap, String queryString);
}
