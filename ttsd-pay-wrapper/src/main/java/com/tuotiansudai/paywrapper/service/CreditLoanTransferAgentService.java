package com.tuotiansudai.paywrapper.service;

import java.util.Map;

public interface CreditLoanTransferAgentService {

    void creditLoanOut();

    String creditLoanOutCallback(Map<String, String> paramsMap, String queryString);
}
