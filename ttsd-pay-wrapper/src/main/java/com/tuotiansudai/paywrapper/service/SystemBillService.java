package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.SystemBillBusinessType;

public interface SystemBillService {

    void transferOut(long amount,String detail,SystemBillBusinessType businessType,String orderId);

}
