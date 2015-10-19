package com.tuotiansudai.paywrapper.service;

import java.util.Map;

public interface SendCloudMailService {


    boolean sendMailByLoanOut(String toAddress,
                              Map<String, String> map);

    boolean sendMailByRepayCompleted(String toAddress,
                              Map<String, String> map);
}
