package com.tuotiansudai.paywrapper.service;

import java.util.Map;

public interface CreditLoanOutService {

    void creditLoanOut();

    String creditLoanOutCallback(Map<String, String> paramsMap, String queryString);
}
