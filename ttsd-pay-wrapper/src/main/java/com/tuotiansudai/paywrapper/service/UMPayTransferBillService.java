package com.tuotiansudai.paywrapper.service;

import java.util.Date;
import java.util.List;

public interface UMPayTransferBillService {
    List<List<String>> getTransferBill(String loginName, Date startDate, Date endDate);
}
