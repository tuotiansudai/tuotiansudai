package com.tuotiansudai.paywrapper.service;

import java.util.Map;

public interface ExperienceRepayService {

    boolean repay(long investId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);
}
