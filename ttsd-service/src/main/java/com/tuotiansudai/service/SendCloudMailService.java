package com.tuotiansudai.service;

import java.util.Map;

public interface SendCloudMailService {


    boolean sendMailByLoanOut(String toAddress,
                              Map<String, String> map);
}
