package com.tuotiansudai.paywrapper.service;

import java.util.Map;

public interface CreditLoanTransferAgentService {

    void creditLoanTransferAgent();

    String creditLoanTransferAgentCallback(Map<String, String> paramsMap, String queryString);
}
