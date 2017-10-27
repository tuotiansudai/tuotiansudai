package com.tuotiansudai.paywrapper.service;

import java.util.Map;

public interface PayrollService {
    void pay(long payrollId);

    String payNotify(Map<String, String> paramsMap, String queryString);
}
